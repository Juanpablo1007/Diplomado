package publickeycipher;

import java.security.*;

import Persintencia.Person;
import util.Util;

public class PublicKeyCipherTester05 {
    public static void main(String[] args) throws Exception {
        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        publicKeyCipher cipher = new publicKeyCipher(algorithm);

        String clearText = "This is a secret message.";
        Person persona = new Person("juan",24,1.74);
        byte[] encryptedText = cipher.encryptObject(persona, publicKey);
        System.out.println("Encrypted object: " + Util.getEncoder().encodeToString(encryptedText));

        Object decryptedText = (Object) cipher.decryptObject(encryptedText, privateKey);
        System.out.println("Decrypted object: " + decryptedText);
    }
}
