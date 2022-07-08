package fr.pixteam.pixcms.utils;

public class Checks {

    private Checks() {
    }

    public static String notBlank(String name, String s) {
        if (s == null || s.trim().isEmpty())
            throw new IllegalStateException(name + " cannot be null or empty.");
        return s;
    }
}
