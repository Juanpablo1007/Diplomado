package filetransfer;

import util.Files;
import integrity.Hasher;
import symmetriccipher.SecretKeyManager;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferServer {
    public static final int PORT = 4000;
    private ServerSocket listener;
    private Socket serverSideSocket;

    public FileTransferServer() {
        System.out.println("Carlos E. Gomez - May 20/2024");
        System.out.println("File transfer server is running on port: " + PORT);
    }

    private void init() throws Exception {
        listener = new ServerSocket(PORT);
        while (true) {
            serverSideSocket = listener.accept();
            protocol(serverSideSocket);
            serverSideSocket.close(); // Ensure the socket is closed after each protocol run
        }
    }

    public void protocol(Socket socket) throws Exception {
        System.out.println("Server: Receiving encrypted file from client...");
        receiveEncryptedFile(socket);

        System.out.println("Server: Sending encrypted file to client...");
        String filename = "server/pollo.png";
        sendEncryptedFile(filename, socket);
    }

    public static void main(String[] args) throws Exception {
        FileTransferServer fts = new FileTransferServer();
        fts.init();
    }

    public static void sendEncryptedFile(String filename, Socket socket) throws Exception {
        SecretKey secretKey = SecretKeyManager.loadKey();
        String encryptedFilename = encryptFile(filename, secretKey);

        Files.sendFile(encryptedFilename, socket);
        Files.sendObject(secretKey.getEncoded(), socket);

        String hashFilename = encryptedFilename + ".hash";
        Hasher.generateIntegrityCheckerFile(filename, hashFilename);
        Files.sendFile(hashFilename, socket);
    }

    public static void receiveEncryptedFile(Socket socket) throws Exception {
        String encryptedFilename = Files.receiveFile("serverReceiver", socket);
        byte[] keyBytes = (byte[]) Files.receiveObject(socket);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "DES");

        // Adding a small delay to ensure the next file is fully sent
        Thread.sleep(1000);

        String hashFilename = Files.receiveFile("serverReceiver", socket);
        String decryptedFilename = decryptFile(encryptedFilename, secretKey);

        Hasher.checkIntegrityFile(decryptedFilename, hashFilename);
        System.out.println("Server: File received and integrity verified.");
    }

    public static String encryptFile(String filename, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        String encryptedFilename = filename + ".encrypted";
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filename));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(encryptedFilename))) {

            byte[] buffer = new byte[512];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] encryptedData = cipher.update(buffer, 0, bytesRead);
                if (encryptedData != null) {
                    outputStream.write(encryptedData);
                }
            }
            byte[] finalBlock = cipher.doFinal();
            if (finalBlock != null) {
                outputStream.write(finalBlock);
            }
        }

        return encryptedFilename;
    }

    public static String decryptFile(String filename, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        String decryptedFilename = "serverReceiver/" + new File(filename).getName().replace(".encrypted", ".decrypted");
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filename));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(decryptedFilename))) {

            byte[] buffer = new byte[512 + 8];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] decryptedData = cipher.update(buffer, 0, bytesRead);
                if (decryptedData != null) {
                    outputStream.write(decryptedData);
                }
            }
            byte[] finalBlock = cipher.doFinal();
            if (finalBlock != null) {
                outputStream.write(finalBlock);
            }
        }
        return decryptedFilename;
    }
}
