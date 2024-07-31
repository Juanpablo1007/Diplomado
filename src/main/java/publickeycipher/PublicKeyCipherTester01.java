package publickeycipher;

import util.Util;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

public class PublicKeyCipherTester01 {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException {
        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        publicKeyCipher cipher = new publicKeyCipher(algorithm);
        String clearText = "In public key cryptography, the same key is used to encrypt and decrypt the text.";
        System.out.println(clearText);
        byte[] encryptedText = cipher.encryptMessage(clearText, publicKey);
        System.out.println(Util.byteArrayToHexString(encryptedText, " "));
        clearText = cipher.decryptMessage(encryptedText, privateKey);
        System.out.println(clearText);

        encryptedText = cipher.encryptMessage(clearText, privateKey);
        System.out.println(Util.byteArrayToHexString(encryptedText, " "));
        clearText = cipher.decryptMessage(encryptedText, publicKey);
        System.out.println(clearText);
    }

}
