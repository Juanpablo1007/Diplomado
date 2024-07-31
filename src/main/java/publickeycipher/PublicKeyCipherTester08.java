package publickeycipher;

import util.Util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PublicKeyCipherTester08 {


    public static void main(String[] args) throws Exception {
        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        publicKeyCipher cipher = new publicKeyCipher(algorithm);

        // Generar un byte array de tamaño mayor al permitido por RSA
        byte[] originalBytes = Util.generateRandomString(200).getBytes();

        // Dividir el byte array en fragmentos
        int chunkSize = 100; // Ajustar según el tamaño permitido por RSA
        byte[][] splittedBytes = Util.split(originalBytes, chunkSize);

        // Cifrar cada fragmento
        byte[][] encryptedFragments = new byte[splittedBytes.length][];
        for (int i = 0; i < splittedBytes.length; i++) {
            encryptedFragments[i] = cipher.encryptMessage(new String(splittedBytes[i]), publicKey);
        }

        // Descifrar cada fragmento
        byte[][] decryptedFragments = new byte[encryptedFragments.length][];
        for (int i = 0; i < encryptedFragments.length; i++) {
            decryptedFragments[i] = cipher.decryptMessage(encryptedFragments[i], privateKey).getBytes();
        }

        // Unir los fragmentos descifrados
        byte[] decryptedBytes = Util.join(decryptedFragments);
        String decryptedString = new String(decryptedBytes);

        // Verificar que el string original y el descifrado sean iguales
        System.out.println("Original and Decrypted Strings are equal: " + new String(originalBytes).equals(decryptedString));
    }

}

