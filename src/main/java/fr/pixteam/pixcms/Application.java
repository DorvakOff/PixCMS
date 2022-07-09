package fr.pixteam.pixcms;

import fr.pixteam.pixcms.mailing.MailManager;
import fr.pixteam.pixcms.managers.Environment;
import fr.pixteam.pixcms.service.UserService;
import fr.pixteam.pixcms.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@Controller
@SpringBootApplication
public class Application {

    public static final String VERSION = "Alpha-1.0";
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static MailManager mailManager;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        List<String> asciiLines = Arrays.asList(
                "",
                "  _____    _           _____   __  __    _____ ",
                " |  __ \\  (_)         / ____| |  \\/  |  / ____|",
                " | |__) |  _  __  __ | |      | \\  / | | (___  ",
                " |  ___/  | | \\ \\/ / | |      | |\\/| |  \\___ \\ ",
                " | |      | |  >  <  | |____  | |  | |  ____) |",
                " |_|      |_| /_/\\_\\  \\_____| |_|  |_| |_____/ ",
                " ".repeat(43 - (3 + VERSION.length())) + "(v" + VERSION + ")",
                ""
        );
        System.out.println(String.join(StringUtils.LINE_SEPARATOR, asciiLines));
        Environment.load();
        AuthManager.initKeyPairs();
        mailManager = new MailManager();

        SpringApplication.run(Application.class, args);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static MailManager getMailManager() {
        return mailManager;
    }

    @Bean
    CommandLineRunner runner(UserService userService) {
        return args -> {
        };
    }
}