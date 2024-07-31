package integrity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;

public class Hasher {
    public static void generateIntegrityCheckerFile(String inputFile, String outputFile) throws Exception {
        MessageDigest hasher = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(inputFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            hasher.update(buffer, 0, bytesRead);
        }
        fis.close();

        byte[] hash = hasher.digest();
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(hash);
        fos.close();
    }

    public static void checkIntegrityFile(String inputFile, String hashFile) throws Exception {
        MessageDigest hasher = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(inputFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            hasher.update(buffer, 0, bytesRead);
        }
        fis.close();

        byte[] calculatedHash = hasher.digest();

        FileInputStream hashFis = new FileInputStream(hashFile);
        byte[] storedHash = new byte[calculatedHash.length];
        hashFis.read(storedHash);
        hashFis.close();

        if (MessageDigest.isEqual(calculatedHash, storedHash)) {
            System.out.println("Integrity check passed. The file is valid.");
        } else {
            System.out.println("Integrity check failed. The file is corrupted or has been tampered with.");
        }
    }
}
