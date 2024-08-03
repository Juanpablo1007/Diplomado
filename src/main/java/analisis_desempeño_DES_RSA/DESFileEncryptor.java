package analisis_desempeño_DES_RSA;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;

public class DESFileEncryptor {
    private Key desKey;

    public DESFileEncryptor() throws Exception {
        this.desKey = generateDESKey();
    }

    public DESFileEncryptor(Key desKey) {
        this.desKey = desKey;
    }

    public Key getDesKey() {
        return desKey;
    }

    private Key generateDESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56); // DES usa una longitud de clave de 56 bits
        SecretKey secretKey = keyGen.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), "DES");
    }

    public long encryptFile(File inputFile, File outputFile) throws Exception {
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, desKey);

        long startTime = System.nanoTime();

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[64];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = desCipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }
            byte[] outputBytes = desCipher.doFinal();
            if (outputBytes != null) {
                fos.write(outputBytes);
            }
        }

        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long decryptFile(File inputFile, File outputFile) throws Exception {
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher.init(Cipher.DECRYPT_MODE, desKey);

        long startTime = System.nanoTime();

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[64];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = desCipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }
            byte[] outputBytes = desCipher.doFinal();
            if (outputBytes != null) {
                fos.write(outputBytes);
            }
        }

        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}

