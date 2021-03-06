package fr.pixteam.pixcms.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    private FileUtils() {
    }

    public static void generateFileIfAbsent(String path, String fileName) {
        generateDirectoriesIfAbsent(path);
        File file = new File(path, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void generateDirectoriesIfAbsent(String path) {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
    }

    public static boolean fileExists(String path, String fileName) {
        return new File(path, fileName).exists();
    }

    public static boolean directoryExists(String path) {
        return fileExists(path, "");
    }
}
