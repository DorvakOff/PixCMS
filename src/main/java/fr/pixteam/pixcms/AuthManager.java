package fr.pixteam.pixcms;

import fr.pixteam.pixcms.utils.FileUtils;
import fr.pixteam.pixcms.utils.PemUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class AuthManager {

    public static final String RSA_PUBLIC_PEM = "rsa-public.pem";
    public static final String RSA_PRIVATE_PEM = "rsa-private.pem";
    private static final String PATH = "PixCMS/generated/auth";
    private static final String ALGO = "RSA";
    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    public static void initKeyPairs() {
        if (FileUtils.fileExists(PATH, RSA_PUBLIC_PEM) && FileUtils.fileExists(PATH, RSA_PRIVATE_PEM)) {
            try {
                publicKey = PemUtils.readPublicKeyFromFile(PATH + File.separator + RSA_PUBLIC_PEM, ALGO);
                privateKey = PemUtils.readPrivateKeyFromFile(PATH + File.separator + RSA_PRIVATE_PEM, ALGO);
                Application.getLogger().info("Retrieved " + ALGO + " keys from " + PATH);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            generateFolderAndFiles();
            createKeys();
            Application.getLogger().info("Generated " + ALGO + " keys in " + PATH);
        }
    }

    private static void createKeys() {

        KeyPair pair = generateKeys();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();

        saveKeyPair(pair);
    }

    private static void saveKeyPair(KeyPair pair) {
        // Private key
        PemObject privateKey = new PemObject("RSA PRIVATE KEY", pair.getPrivate().getEncoded());
        try (
                FileOutputStream os = new FileOutputStream(PATH + File.separator + RSA_PRIVATE_PEM);
                OutputStreamWriter osw = new OutputStreamWriter(os);
                PemWriter writer = new PemWriter(osw)
        ) {
            writer.writeObject(privateKey);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Public key
        PemObject publicKey = new PemObject("RSA PUBLIC KEY", pair.getPublic().getEncoded());
        try (
                FileOutputStream os = new FileOutputStream(PATH + File.separator + RSA_PUBLIC_PEM);
                OutputStreamWriter osw = new OutputStreamWriter(os);
                PemWriter writer = new PemWriter(osw)
        ) {
            writer.writeObject(publicKey);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static KeyPair generateKeys() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGO);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
        }
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private static void generateFolderAndFiles() {
        FileUtils.generateFileIfAbsent(PATH, RSA_PUBLIC_PEM);
        FileUtils.generateFileIfAbsent(PATH, RSA_PRIVATE_PEM);
    }

    public static PrivateKey getPrivateKey() {
        return privateKey;
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }
}
