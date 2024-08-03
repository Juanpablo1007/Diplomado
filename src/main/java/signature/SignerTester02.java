package signature;

import util.Util;

import java.io.File;
import java.io.IOException;
import java.security.*;

public class SignerTester02 {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, NoSuchAlgorithmException, IOException {
        String algorithm = "SHA256withRSA";
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair pair = keyPairGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        String filename = "scan.pdf";
        File file = new File(filename);
        byte[] digitalSignature = Signer.signFile(file, algorithm, privateKey);
        System.out.println(Util.byteArrayToHexString(digitalSignature));

        boolean isVerified = Signer.verifyFileSignature(file, algorithm, publicKey, digitalSignature);
        System.out.println("Firma verificada: " + isVerified);
    }


}
