package fr.pixteam.pixcms.managers;

import org.jasypt.util.password.StrongPasswordEncryptor;

import java.util.Random;

public class AuthManager {

    private static final StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();

    private AuthManager() {
    }

    public static String encodePassword(String passwordRaw) {
        return encryptor.encryptPassword(passwordRaw);
    }

    public static boolean passwordMatch(String raw, String encoded) {
        return encryptor.checkPassword(raw, encoded);
    }

    public static String generateVerificationCode() {
        int length = 128;
        StringBuilder code = new StringBuilder();
        Random r = new Random();
        char ch;
        for (int i = 0; i < length; i++) {
            int type = r.nextInt(3);
            switch (type) {
                case 0:
                    ch = (char) (r.nextInt(26) + 65);
                    code.append(ch);
                    break;

                case 1:
                    ch = (char) (r.nextInt(26) + 97);
                    code.append(ch);
                    break;

                case 2:
                    code.append(r.nextInt(10));
                    break;
            }
        }
        return code.toString();
    }
}
