package fr.pixteam.pixcms.controller;

import fr.pixteam.pixcms.model.User;
import fr.pixteam.pixcms.service.UserService;
import fr.pixteam.pixcms.utils.Checks;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public @NotNull Iterable<User> list() {
        return this.userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserForm form) {
        User user = new User();
        user.setUsername(Checks.notBlank("username", form.getUsername()));

        userService.create(user);

        String uri = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/users/{id}")
                .buildAndExpand(user.getId())
                .toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return new ResponseEntity<>(user, headers, HttpStatus.CREATED);
    }


    public static class UserForm {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}
