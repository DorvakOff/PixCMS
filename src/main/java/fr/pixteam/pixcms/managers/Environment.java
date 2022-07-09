package fr.pixteam.pixcms.managers;

import fr.pixteam.pixcms.Application;
import fr.pixteam.pixcms.utils.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.validation.constraints.NotNull;

public enum Environment {

    MAILING_ENABLED("false"),

    SMTP_HOST("host.smtp.com"),

    SMTP_PORT("587"),

    OUTGOING_EMAIL_ADDRESS("email@host.com"),

    SMTP_PASSWORD("password"),

    ;

    private String value;
    private String defaultValue;

    Environment(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    Environment() {
    }

    public static void load() {
        File file = new File("PixCMS" + File.separator + "configuration", ".env");

        FileUtils.generateFileIfAbsent(file);

        HashMap<String, String> keySet = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace(" ", "");
                String key = line.split("=")[0];
                String value = line.replaceFirst(key + "=", "");
                keySet.put(key, value);
            }
        } catch (IOException e) {
            Application.getLogger().error("An error occurred", e);
        }

        if (missingValue(keySet)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                int i = 0;
                for (Environment env : values()) {
                    if (!keySet.containsKey(env.toString())) {
                        if (i > 0) {
                            writer.newLine();
                            writer.newLine();
                        }
                        writer.append(String.valueOf(env)).append(" = ").append(env.defaultValue);
                        keySet.put(env.toString(), env.defaultValue);
                    }
                    i++;
                }
            } catch (IOException e) {
                Application.getLogger().error("An error occurred", e);
            }
        }

        for (Environment env : values()) {
            env.set(keySet.get(env.toString()));
        }
    }

    private static boolean missingValue(@NotNull HashMap<String, String> keySet) {
        for (Environment environment : values()) {
            if (!keySet.containsKey(environment.toString())) {
                return true;
            }
        }
        return false;
    }

    private void set(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

}