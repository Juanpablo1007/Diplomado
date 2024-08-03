package analisis_desempeño_DES_RSA;
import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAFileEncryptor {
    private PublicKey rsaPublicKey;
    private PrivateKey rsaPrivateKey;
    private int keySize;

    public RSAFileEncryptor(PublicKey rsaPublicKey, PrivateKey rsaPrivateKey, int keySize) {
        this.rsaPublicKey = rsaPublicKey;
        this.rsaPrivateKey = rsaPrivateKey;
        this.keySize = keySize;
    }

    public long encryptFile(File inputFile, File outputFile) throws Exception {
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);

        int bufferSize = (keySize / 8) - 11; // Ajustar para el padding
        long startTime = System.nanoTime();

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[bufferSize];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = rsaCipher.doFinal(buffer, 0, bytesRead);
                fos.write(output);
            }
        }

        long endTime = System.nanoTime();
        return (endTime - startTime);
    }

    public long decryptFile(File inputFile, File outputFile) throws Exception {
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);

        int bufferSize = keySize / 8;
        long startTime = System.nanoTime();

        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[bufferSize];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = rsaCipher.doFinal(buffer, 0, bytesRead);
                fos.write(output);
            }
        }

        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}
