package fr.pixteam.pixcms.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class MailUtils {

    private MailUtils() {
    }

    private static String getTemplate(String name, String... placeholders) {
        String lines = null;
        try (InputStream url = Thread.currentThread().getContextClassLoader().getResourceAsStream(name + ".html"); BufferedReader br = new BufferedReader(new InputStreamReader(url))) {
            lines = br.lines().collect(Collectors.joining());
            for (int i = 0; i < placeholders.length; i++) {
                lines = lines.replace("{%" + i + "%}", placeholders[i]);
            }
        } catch (Exception ignored) {
        }
        return lines;
    }

    public static String getVerifyEmailTemplate(String username, String verificationUrl) {
        return getTemplate("verify-email", username, verificationUrl);
    }
}
