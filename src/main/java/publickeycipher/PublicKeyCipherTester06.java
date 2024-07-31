package publickeycipher;

import Persintencia.Person;
import util.Util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PublicKeyCipherTester06 {
    public static void main(String[] args) throws Exception {

        String randomString = Util.generateRandomString(100);
        System.out.println("Random String: " + randomString);


        byte[] originalBytes = randomString.getBytes();


        int chunkSize = 20;
        byte[][] splittedBytes = Util.split(originalBytes, chunkSize);


        for (int i = 0; i < splittedBytes.length; i++) {
            System.out.println("Fragment " + i + ": " + new String(splittedBytes[i]));
        }


        byte[] joinedBytes = Util.join(splittedBytes);
        String joinedString = new String(joinedBytes);

        // Verificar que el string original y el unido sean iguale
        System.out.println("Joined String: " + joinedString);
        System.out.println("Original and Joined Strings are equal: " + randomString.equals(joinedString));
    }
}
