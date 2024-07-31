package Network;

import Persintencia.Usuario;
import symmetriccipher.SecretKeyManager;
import symmetriccipher.SymetricCipher;
import util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static util.Files.receiveObject;
import static util.Files.sendObject;


public class FileTransferClient {
    public static final int PORT = 4000;
    public static final String SERVER = "localhost";

    private static final int BLOCK_SIZE = 512;


    private Socket clientSideSocket;
    private String server;
    private int port;

    public FileTransferClient() {
        this.server = SERVER;
        this.port = PORT;
        System.out.println("Carlos E. Gomez - May 20/2024");
        System.out.println("File transfer client is running ... connecting the server in " + this.server + ":" + this.port);
    }

    public FileTransferClient(String server, int port) {
        this.server = server;
        this.port = port;
        System.out.println("Carlos E. Gomez - May 20/2024");
        System.out.println("File transfer client is running ... connecting the server in " + this.server + ":" + this.port);
    }

    public void init() throws Exception {
        clientSideSocket = new Socket(server, port);
        Usuario usuario = new Usuario("kevin",10000.0);

        sendObject(usuario, clientSideSocket);
        String mensaje = (String) receiveObject( clientSideSocket);
        System.out.println(mensaje);
        clientSideSocket.close();

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

    public static String encryptTextFile(String filename) throws Exception {
        String contenido = readFileAsString(filename);
        SecretKey secretKey = SecretKeyManager.loadKey();
        SymetricCipher cipher = new SymetricCipher(secretKey, "DES/ECB/PKCS5Padding");
        String[] linea = contenido.split("\n");
        StringBuilder encryptedB64 = new StringBuilder();
        for (int i = 0; i < linea.length; i++) {
            byte[] encryptedText = cipher.encryptMessage(linea[i]);
            encryptedB64.append(Base64.encode(encryptedText));
            if (i < linea.length - 1) {
                encryptedB64.append("\n");
            }
        }
        String path = pathToEncrypted(filename);
        writeStringToFile(encryptedB64.toString(), path);
        return path;
    }

    private static String pathToEncrypted(String path){
        return path + ".encrypted";
    }

    private static String pathToDecrypted(String path) {
        String[] r = path.split("\\.");
        return r[0] + ".plain." + r[r.length - 2];
    }

    public static String decryptTextFile(String filename) throws Exception {
        String contenido = readFileAsString(filename);
        SecretKey secretKey = SecretKeyManager.loadKey();
        SymetricCipher cipher = new SymetricCipher(secretKey, "DES/ECB/PKCS5Padding");
        System.out.println(contenido);
        String[] linea = contenido.split("\n");
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < linea.length; i++) {
            System.out.println(linea[i]);
            byte[] decryptedBytes = Base64.decode(linea[i]);
            decryptedText.append(cipher.decryptMessage(decryptedBytes));
            if (i < linea.length - 1) {
                decryptedText.append("\n");
            }
        }
        String path = pathToDecrypted(filename);
        writeStringToFile(decryptedText.toString(), path);
        return path;
    }

    public static String readFileAsString(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writeStringToFile(String content, String filePath) {
        try {
            Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String encryptBinaryFile(String filename) throws Exception {
        SecretKey secretKey = SecretKeyManager.loadKey();
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filename));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(pathToEncrypted(filename)))) {

            byte[] buffer = new byte[BLOCK_SIZE];
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

        return filename + ".encrypted";
    }

    public static String decryptBinaryFile(String filename) throws Exception {
        SecretKey secretKey = SecretKeyManager.loadKey();
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);



        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filename));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(pathToDecrypted(filename)))) {

            byte[] buffer = new byte[BLOCK_SIZE + 8];
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

        return pathToDecrypted(filename);
    }
}
