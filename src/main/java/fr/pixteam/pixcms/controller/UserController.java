package fr.pixteam.pixcms.controller;

import fr.pixteam.pixcms.Application;
import fr.pixteam.pixcms.managers.AuthManager;
import fr.pixteam.pixcms.model.AccountStatus;
import fr.pixteam.pixcms.model.User;
import fr.pixteam.pixcms.service.UserService;
import fr.pixteam.pixcms.utils.Checks;
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
import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/users")
public class UserController {
    UserService userService;

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

        userService.create(user);

        String code = AuthManager.generateVerificationCode();

        try {
            Application.getMailManager().sendMail(
                    "Verify your email",
                    MailUtils.getVerifyEmailTemplate(
                            user.getUsername(),
                            ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/login?verificationCode=" + code
                    ),
                    Collections.singletonList(user.getEmail())
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static class UserForm {
        private String username;
        private String email;
        private String password;

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
    }

}
