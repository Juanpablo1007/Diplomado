package analisis_desempeño_DES_RSA;

import java.io.File;
import java.util.Objects;

public class Analisis {
    private static String directoryArchivos = "src/main/java/analisis_desempeño_DES_RSA/archivos/";
    private static String directoryEncrypt = "src/main/java/analisis_desempeño_DES_RSA/archivos/encriptado/";
    private static String directoryDecrypt = "src/main/java/analisis_desempeño_DES_RSA/archivos/desencriptado/";

    public static void main(String[] args) throws Exception {
        String inputDirectoryPath = "src/main/java/analisis_desempeño_DES_RSA/archivos/original"; // Cambia esto por la ruta del directorio que deseas recorrer
        File inputDirectory = new File(inputDirectoryPath);
        File csvFile = new File(directoryArchivos,"encryption_times.csv");
        FileManager.createCsvWithHeader(csvFile, "Archivo,Tamaño,Encr DES,Desencr DES,Encr RSA 1024,Desencr RSA 1024,Encr RSA 2048,Desencr RSA 2048");

        if (inputDirectory.isDirectory()) {
            for (File inputFile : Objects.requireNonNull(inputDirectory.listFiles())) {
                if (inputFile.isFile()) {
                    int size = (int) inputFile.length();

                    // DES
                    DESFileEncryptor desEncryptor = new DESFileEncryptor();
                    File encryptedFileDES = new File(directoryEncrypt, "encrypted_des_" + inputFile.getName());
                    File decryptedFileDES = new File(directoryDecrypt, "decrypted_des_" + inputFile.getName());

                    long encryptTimeDES = desEncryptor.encryptFile(inputFile, encryptedFileDES);
                    long decryptTimeDES = desEncryptor.decryptFile(encryptedFileDES, decryptedFileDES);


                    // RSA de 1024 bits
                    RSAKeyPairGenerator keyPairGenerator1024 = new RSAKeyPairGenerator(1024);
                    RSAFileEncryptor fileEncryptor1024 = new RSAFileEncryptor(keyPairGenerator1024.getPublicKey(), keyPairGenerator1024.getPrivateKey(), 1024);
                    File encryptedFile1024 = new File(directoryEncrypt, "encrypted_rsa_1024_" + inputFile.getName());
                    File decryptedFile1024 = new File(directoryDecrypt, "decrypted_rsa_1024_" + inputFile.getName());

                    long encryptTime1024 = fileEncryptor1024.encryptFile(inputFile, encryptedFile1024);
                    long decryptTime1024 = fileEncryptor1024.decryptFile(encryptedFile1024, decryptedFile1024);

                    // RSA de 2048 bits
                    RSAKeyPairGenerator keyPairGenerator2048 = new RSAKeyPairGenerator(2048);
                    RSAFileEncryptor fileEncryptor2048 = new RSAFileEncryptor(keyPairGenerator2048.getPublicKey(), keyPairGenerator2048.getPrivateKey(), 2048);
                    File encryptedFile2048 = new File(directoryEncrypt, "encrypted_rsa_2048_" + inputFile.getName());
                    File decryptedFile2048 = new File(directoryDecrypt, "decrypted_rsa_2048_" + inputFile.getName());

                    long encryptTime2048 = fileEncryptor2048.encryptFile(inputFile, encryptedFile2048);
                    long decryptTime2048 = fileEncryptor2048.decryptFile(encryptedFile2048, decryptedFile2048);

                    String csvLine = inputFile.getName() + "," + size + "," + encryptTimeDES + "," + decryptTimeDES + "," + encryptTime1024 + "," + decryptTime1024 + "," + encryptTime2048 + "," + decryptTime2048;
                    FileManager.appendToCsv(csvFile, csvLine);
                }
            }
        } else {
            System.err.println("El directorio de entrada especificado no es válido.");
        }
    }
}
