package com.contactsunny.poc.JavaRSAEncryptionPOC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.Cipher;
import java.io.File;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.contactsunny.poc.JavaRSAEncryptionPOC.config.CustomConstants.*;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Value("${keys.publicKey.path}")
    private String publicKeyPath;
    
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        String dataToBeEncrypted = "Some random words in no particular order.";
        byte[] bytesToBeEncrypted = dataToBeEncrypted.getBytes();

        try {
            byte[] encryptedByteArray = encrypt(bytesToBeEncrypted);

            String encryptedString = Base64.getEncoder().encodeToString(encryptedByteArray);

            logger.info("Encrypted: " + encryptedString);
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("It did not work!");
        }
        
        logger.info("We're done here!");
    }

    private byte[] encrypt(byte[] inputByteArray) throws Throwable {

        File keyFile = new File(publicKeyPath);
        byte[] publicKey = Files.readAllBytes(keyFile.toPath());

        String keyString = new String(publicKey);
        keyString = keyString.replaceAll(NEW_LINE_CHARACTER, EMPTY_STRING)
                .replaceAll(PUBLIC_KEY_START_KEY_STRING, EMPTY_STRING)
                .replaceAll(PUBLIC_KEY_END_KEY_STRING, EMPTY_STRING);

        publicKey = keyString.getBytes();

        Key generatePublic = KeyFactory.getInstance(KEY_FACTORY_INSTANCE_TYPE).
                generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));

        Cipher cipherInstance = Cipher.getInstance(CIPHER_INSTANCE_TYPE);
        cipherInstance.init(1, generatePublic);

        return cipherInstance.doFinal(inputByteArray);
    }
}
