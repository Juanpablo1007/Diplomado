package publickeycipher;

import util.Util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PublicKeyCipherTester09 {


    public static void main(String[] args) {
        int[] keySizes = {1024, 2048, 3072, 4096};
        int paddingSize = 11; // Para PKCS#1 v1.5

        System.out.println("Tamaño de la llave\tTamaño máximo de los datos a cifrar con RSA");
        for (int keySize : keySizes) {
            int maxDataSize = (keySize / 8) - paddingSize;
            System.out.println(keySize + "\t\t" + maxDataSize);
        }
    }

}

