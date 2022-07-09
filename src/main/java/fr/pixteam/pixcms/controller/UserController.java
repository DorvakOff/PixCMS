package fr.pixteam.pixcms.controller;

import fr.pixteam.pixcms.Application;
import fr.pixteam.pixcms.managers.AuthManager;
import fr.pixteam.pixcms.model.AccountStatus;
import fr.pixteam.pixcms.model.User;
import fr.pixteam.pixcms.service.UserService;
import fr.pixteam.pixcms.utils.Checks;
import fr.pixteam.pixcms.utils.ExpirableConcurrentHashMap;
import fr.pixteam.pixcms.utils.MailUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ExpirableConcurrentHashMap<String, String> userValidationCodes = new ExpirableConcurrentHashMap<>(30, TimeUnit.SECONDS);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @NotNull Iterable<User> list() {
        return this.userService.getAllUsers();
    }

    @PostMapping
    @RequestMapping("/login")
    void login(@RequestBody UserForm form) {
        User user = userService.find(Checks.notBlank("username", form.getUsername()));

        if (user == null || !AuthManager.passwordMatch(Checks.notBlank("password", form.getPassword()), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Entered credentials does not match a valid username/password");
        }
        if (user.getAccountStatus() == AccountStatus.WAITING_VERIFICATION) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please verify your account before doing this!");
        }
    }


    @PostMapping
    @RequestMapping("/register")
    void register(@RequestBody UserForm form) {
        User user = new User(
                Checks.notBlank("username", form.getUsername()),
                Checks.notBlank("email", form.getEmail()),
                Checks.notBlank("password", form.getPassword())
        );

        String code = getVerificationCode();

        try {
            String template = MailUtils.getVerifyEmailTemplate(
                    user.getUsername(),
                    ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/login?verificationCode=" + code
            );
            userService.create(user);
            userValidationCodes.put(user.getUsername(), code);
            Application.getMailManager().sendMail(
                    "Verify your email",
                    template
                    ,
                    Collections.singletonList(user.getEmail())
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getVerificationCode() {
        String code = AuthManager.generateVerificationCode();
        if (userValidationCodes.containsValue(code)) {
            code = getVerificationCode();
        }
        return code;
    }

    @PostMapping
    @RequestMapping("/validate")
    void validate(@RequestBody UserForm form) {
        String code = Checks.notBlank("verificationCode", form.getVerificationCode());
        if (!userValidationCodes.containsValue(code))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This verification code is too old or does not exists.");

        String username = userValidationCodes.entrySet().stream().filter(entry -> code.equals(entry.getValue())).map(Map.Entry::getKey).findFirst().orElse(null);

        User user = userService.find(Checks.notBlank("username", username));
        user.setAccountStatus(AccountStatus.VERIFIED);
        userService.update(user);
    }

    public static class UserForm {
        private String username;
        private String email;
        private String password;
        private String verificationCode;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
        }
    }

}
