package publickeycipher;

import util.Util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class publicKeyCipher {

    private  Cipher cipher ;

    public publicKeyCipher(String algorithm) throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipher = Cipher.getInstance(algorithm);
    }
    public byte[] encryptObject(Object input, Key key) throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] clearObject = Util.objectToByteArray(input);
        return cipher.doFinal(clearObject);
    }

    public Object decryptObject(byte[] input, Key key) throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] clearObject = cipher.doFinal(input);
        return Util.byteArrayToObject(clearObject);
    }



    public byte[] encryptMessage(String input, Key key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, BadPaddingException {
        byte[] cipherText = null;
        byte[] clearText = input.getBytes();
       // System.out.println(cipher == null);

        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(clearText);

        return cipherText;
    }

    public String decryptMessage(byte[] input, Key key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String output = "";

        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] clearText = cipher.doFinal(input);
        output = new String(clearText);

        return output;
    }



}
