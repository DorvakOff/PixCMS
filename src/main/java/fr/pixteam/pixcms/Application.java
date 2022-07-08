package fr.pixteam.pixcms;

import fr.pixteam.pixcms.model.User;
import fr.pixteam.pixcms.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner runner(UserService userService) {
        return args -> {
            userService.create(new User("Dorvak"));
            userService.create(new User("WarnD"));
            userService.getAllUsers().forEach(System.out::println);
        };
    }
}