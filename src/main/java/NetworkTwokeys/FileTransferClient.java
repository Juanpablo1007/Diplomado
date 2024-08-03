package NetworkTwokeys;

import asymmetriccipher.RSAKeyManager;
import integrity.Hasher;
import util.Files;

import javax.crypto.Cipher;
import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static NetworkTwokeys.FileTransferServer.savePrivateKey;

public class FileTransferClient {
    public static final int PORT = 4000;
    public static final String SERVER = "localhost";

    private Socket clientSideSocket;
    private String server;
    private int port;

    public FileTransferClient() {
        this.server = SERVER;
        this.port = PORT;
        System.out.println("Carlos E. Gomez - May 20/2024");
        System.out.println("File transfer client is running ... connecting to the server at " + this.server + ":" + this.port);
    }

    public FileTransferClient(String server, int port) {
        this.server = server;
        this.port = port;
        System.out.println("Carlos E. Gomez - May 20/2024");
        System.out.println("File transfer client is running ... connecting to the server at " + this.server + ":" + this.port);
    }

    public void init() throws Exception {
        clientSideSocket = new Socket(server, port);
        protocol(clientSideSocket);
        clientSideSocket.close();
    }

    public void protocol(Socket socket) throws Exception {
        // Recibir la llave pública del servidor
        byte[] serverPublicKeyBytes = (byte[]) Files.receiveObject(socket);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey serverPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(serverPublicKeyBytes));
        savePublicKey(serverPublicKey, "clientReceiver/serverPublicKey.pem");

        KeyPair keyPair = RSAKeyManager.loadKeyPair();
        PublicKey clientPublicKey = keyPair.getPublic();
        PrivateKey clientPrivateKey = keyPair.getPrivate();
        savePrivateKey(clientPrivateKey,"clientReceiver/clientPrivateKey.pem" );
        // Enviar la llave pública del cliente al servidor
        Files.sendObject(clientPublicKey.getEncoded(), socket);
        savePublicKey(clientPublicKey, "clientReceiver/clientPublicKey.pem");

        System.out.println("Client: Sending encrypted file to server...");
        String filename = "img.png";
        sendEncryptedFile(filename, socket, serverPublicKey);

        System.out.println("Client: Receiving encrypted file from server...");
        receiveEncryptedFile(socket, clientPrivateKey);
    }



    public static void main(String[] args) throws Exception {
        FileTransferClient ftc;
        if (args.length == 0) {
            ftc = new FileTransferClient();
        } else {
            String server = args[0];
            int port = Integer.parseInt(args[1]);
            ftc = new FileTransferClient(server, port);
        }
        ftc.init();
    }

    public static void sendEncryptedFile(String filename, Socket socket, PublicKey serverPublicKey) throws Exception {
        // Encriptar el archivo por segmentos
        List<byte[]> encryptedData = encryptFile(filename, serverPublicKey);

        // Enviar los segmentos cifrados
        Files.sendObject(encryptedData, socket);

        // Generar y enviar el archivo de hash
        String hashFilename = generateHashFile(filename, "clientReceiver");
        Files.sendFile(hashFilename, socket);
    }

    public static void receiveEncryptedFile(Socket socket, PrivateKey clientPrivateKey) throws Exception {
        List<byte[]> encryptedData = (List<byte[]>) Files.receiveObject(socket);

        // Desencriptar los segmentos y unirlos
        String decryptedFilename = decryptFile(encryptedData, clientPrivateKey, "clientReceiver");

        // Recibir el archivo de hash y verificar la integridad
        String hashFilename = Files.receiveFile("clientReceiver", socket);
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
}
