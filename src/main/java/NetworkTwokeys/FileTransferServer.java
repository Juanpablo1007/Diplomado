package NetworkTwokeys;

import asymmetriccipher.RSAKeyManager;
import integrity.Hasher;
import util.Files;

import javax.crypto.Cipher;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
        KeyPair keyPair = RSAKeyManager.loadKeyPair();
        PublicKey serverPublicKey = keyPair.getPublic();
        PrivateKey serverPrivateKey = keyPair.getPrivate();

        // Enviar la llave pública al cliente
        Files.sendObject(serverPublicKey.getEncoded(), socket);
        savePublicKey(serverPublicKey, "serverReceiver/serverPublicKey.pem");
        savePrivateKey(serverPrivateKey,"serverReceiver/serverPrivateKey.pem" );

        // Recibir la llave pública del cliente
        byte[] clientPublicKeyBytes = (byte[]) Files.receiveObject(socket);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey clientPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(clientPublicKeyBytes));
        savePublicKey(clientPublicKey, "serverReceiver/clientPublicKey.pem");

        System.out.println("Server: Receiving encrypted file from client...");
        receiveEncryptedFile(socket, serverPrivateKey);

        System.out.println("Server: Sending encrypted file to client...");
        String filename = "server/pollo.png";
        sendEncryptedFile(filename, socket, clientPublicKey);
    }

    public static void main(String[] args) throws Exception {
        FileTransferServer fts = new FileTransferServer();
        fts.init();
    }

    public static void sendEncryptedFile(String filename, Socket socket, PublicKey clientPublicKey) throws Exception {
        // Encriptar el archivo por segmentos
        List<byte[]> encryptedData = encryptFile(filename, clientPublicKey);

        // Enviar los segmentos cifrados
        Files.sendObject(encryptedData, socket);

        // Generar y enviar el archivo de hash
        String hashFilename = generateHashFile(filename, "serverReceiver");
        Files.sendFile(hashFilename, socket);
    }

    public static void receiveEncryptedFile(Socket socket, PrivateKey serverPrivateKey) throws Exception {
        List<byte[]> encryptedData = (List<byte[]>) Files.receiveObject(socket);

        // Desencriptar los segmentos y unirlos
        String decryptedFilename = decryptFile(encryptedData, serverPrivateKey, "serverReceiver");

        // Recibir el archivo de hash y verificar la integridad
        String hashFilename = Files.receiveFile("serverReceiver", socket);
        Hasher.checkIntegrityFile(decryptedFilename, hashFilename);
    }

    public static List<byte[]> encryptFile(String filename, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        List<byte[]> encryptedSegments = new ArrayList<>();

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] buffer = new byte[245]; // RSA block size
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] segment = new byte[bytesRead];
                System.arraycopy(buffer, 0, segment, 0, bytesRead);
                byte[] encryptedSegment = cipher.doFinal(segment);
                encryptedSegments.add(encryptedSegment);
            }
        }

        return encryptedSegments;
    }

    public static String decryptFile(List<byte[]> encryptedSegments, PrivateKey privateKey, String outputDirectory) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        File dir = new File(outputDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String decryptedFilename = outputDirectory + "/decryptedFile.png";
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(decryptedFilename))) {
            for (byte[] encryptedSegment : encryptedSegments) {
                byte[] decryptedSegment = cipher.doFinal(encryptedSegment);
                outputStream.write(decryptedSegment);
            }
        }

        return decryptedFilename;
    }

    public static String generateHashFile(String filename, String outputDirectory) throws Exception {
        String hashFilename = outputDirectory + "/" + new File(filename).getName() + ".hash";
        Hasher.generateIntegrityCheckerFile(filename, hashFilename);
        return hashFilename;
    }

    public static void savePublicKey(PublicKey publicKey, String filepath) throws IOException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        String keyString = "-----BEGIN PUBLIC KEY-----\n" + Base64.getMimeEncoder().encodeToString(x509EncodedKeySpec.getEncoded()) + "\n-----END PUBLIC KEY-----";
        java.nio.file.Files.write(Paths.get(filepath), keyString.getBytes(), StandardOpenOption.CREATE);
    }
    public static void savePrivateKey(PrivateKey privateKey, String filepath) throws IOException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        String keyString = "-----BEGIN PRIVATE KEY-----\n" + Base64.getMimeEncoder().encodeToString(pkcs8EncodedKeySpec.getEncoded()) + "\n-----END PRIVATE KEY-----";
        java.nio.file.Files.write(Paths.get(filepath), keyString.getBytes(), StandardOpenOption.CREATE);
    }
}
