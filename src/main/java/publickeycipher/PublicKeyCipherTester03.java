package publickeycipher;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.security.*;

public class PublicKeyCipherTester03 {
    public static void main(String[] args) throws Exception {
        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Guardar llaves
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("publicKey"))) {
            oos.writeObject(keyPair.getPublic());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("privateKey"))) {
            oos.writeObject(keyPair.getPrivate());
        }

        // Guardar texto cifrado
        publicKeyCipher cipher = new publicKeyCipher(algorithm);
        String clearText = "Esto es un ejemplo de un texto en claro, si sale asi es porque esta melo todo :)";
        byte[] encryptedText = cipher.encryptMessage(clearText, keyPair.getPublic());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("encryptedText"))) {
            oos.writeObject(encryptedText);
        }

        System.out.println("Keys and encrypted text saved.");
    }
}
