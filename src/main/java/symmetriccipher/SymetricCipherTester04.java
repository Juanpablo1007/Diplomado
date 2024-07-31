package symmetriccipher;

import util.Util;

import javax.crypto.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SymetricCipherTester04 {

    public static void main(String[] args) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException,
            IOException,
            ClassNotFoundException {
        SecretKey secretKey = null;

        secretKey = KeyGenerator.getInstance("DES").generateKey();
        SymetricCipher cipher = new SymetricCipher(secretKey, "DES/ECB/PKCS5Padding");

        ArrayList<String> clearObject = new ArrayList<String>();
        byte[] encryptedObject = null;

        clearObject.add("Ana");
        clearObject.add("Bety");
        clearObject.add("Carolina");
        clearObject.add("Daniela");
        clearObject.add("Elena");

        System.out.println(clearObject);

        encryptedObject = cipher.encryptObject(clearObject);
        System.out.println(Util.byteArrayToHexString(encryptedObject, " "));

        clearObject = (ArrayList<String>) cipher.decryptObject(encryptedObject);
        System.out.println(clearObject);
    }


}
