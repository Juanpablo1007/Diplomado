package signature;

import util.Util;

import java.security.*;

public class SignerTester01 {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String algorithm = "SHA256withRSA";
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair pair = keyPairGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        String message = "This is a secret message";
        byte[] digitalSignature = Signer.signMessage(message, algorithm, privateKey);
        System.out.println(Util.byteArrayToHexString(digitalSignature));

        boolean isVerified = Signer.verifyMessageSignature(message, algorithm, publicKey, digitalSignature);
        System.out.println("Firma verificada: " + isVerified);
    }

}
