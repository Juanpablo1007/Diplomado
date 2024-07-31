package symmetriccipher;
import util.Util;

import javax.crypto.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.*;
import java.util.*;
public class SymetricCipherTester02 {
    public static void main(String[] args) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException,
            IOException {
        SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
        SymetricCipher cipher = new SymetricCipher(secretKey, "DES/ECB/PKCS5Padding");

        String clearText = "In symmetric key cryptography, the same key is used to encrypt and decrypt the clear text.";
        System.out.println(clearText);

        byte[] encryptedText = cipher.encryptMessage(clearText);
        System.out.println(Util.byteArrayToHexString(encryptedText, " "));

        Util.saveObject(secretKey, "secretKey.key");
        System.out.println("The secret key has been saved");

        Util.saveObject(encryptedText, "text.encrypted");
        System.out.println("The encrypted text has been saved");
    }

}
