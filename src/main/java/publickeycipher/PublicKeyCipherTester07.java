package publickeycipher;

import util.Util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PublicKeyCipherTester07 {

    public static void main(String[] args) throws Exception {
        // Generar un string aleatorio de un tamaño específico
        String randomString = Util.generateRandomString(100);
        System.out.println("Random String: " + randomString);

        // Convertir el string a bytes
        byte[] originalBytes = randomString.getBytes();

        // Dividir el byte array en fragmentos
        int chunkSize = 20;
        byte[][] splittedBytes = Util.split(originalBytes, chunkSize);

        // Unir los fragmentos nuevamente
        byte[] joinedBytes = Util.join(splittedBytes);
        String joinedString = new String(joinedBytes);

        // Verificar que el string original y el unido sean iguales
        System.out.println("Joined String: " + joinedString);
        System.out.println("Original and Joined Strings are equal: " + randomString.equals(joinedString));
    }
}
