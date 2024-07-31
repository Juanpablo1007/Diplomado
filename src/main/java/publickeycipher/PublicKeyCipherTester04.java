package publickeycipher;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.*;

public class PublicKeyCipherTester04 {
    public static void main(String[] args) throws Exception {
        String algorithm = "RSA";

        // Recuperar llaves
        PublicKey publicKey;
        PrivateKey privateKey;
        byte[] encryptedText;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("publicKey"))) {
            publicKey = (PublicKey) ois.readObject();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("privateKey"))) {
            privateKey = (PrivateKey) ois.readObject();
        }

        // Recuperar texto cifrado
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("encryptedText"))) {
            encryptedText = (byte[]) ois.readObject();
        }

        publicKeyCipher cipher = new publicKeyCipher(algorithm);
        String clearText = cipher.decryptMessage(encryptedText, privateKey);
        System.out.println(clearText);
    }
}

