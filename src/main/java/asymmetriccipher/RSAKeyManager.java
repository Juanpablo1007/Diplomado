package asymmetriccipher;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RSAKeyManager {

    private static final String KEY_PAIR_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;
    private static final String PUBLIC_KEY_FILE = "publicKey.pem";
    private static final String PRIVATE_KEY_FILE = "privateKey.pem";

    public static KeyPair loadKeyPair() throws Exception {
        if (Files.exists(Paths.get(PUBLIC_KEY_FILE)) && Files.exists(Paths.get(PRIVATE_KEY_FILE))) {
            return new KeyPair(loadPublicKey(), loadPrivateKey());
        } else {
            return generateKeyPair();
        }
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_PAIR_ALGORITHM);
        keyGen.initialize(KEY_SIZE);
        KeyPair keyPair = keyGen.generateKeyPair();

        savePublicKey(keyPair.getPublic());
        savePrivateKey(keyPair.getPrivate());

        return keyPair;
    }

    public static void savePublicKey(PublicKey publicKey) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        String keyString = "-----BEGIN PUBLIC KEY-----\n" + Base64.getMimeEncoder().encodeToString(x509EncodedKeySpec.getEncoded()) + "\n-----END PUBLIC KEY-----";
        Files.write(Paths.get(PUBLIC_KEY_FILE), keyString.getBytes());
    }

    public static PublicKey loadPublicKey() throws Exception {
        String keyString = new String(Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE)));
        keyString = keyString.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");
        byte[] keyBytes = Base64.getMimeDecoder().decode(keyString);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR_ALGORITHM);
        return keyFactory.generatePublic(spec);
    }

    public static void savePrivateKey(PrivateKey privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        String keyString = "-----BEGIN PRIVATE KEY-----\n" + Base64.getMimeEncoder().encodeToString(pkcs8EncodedKeySpec.getEncoded()) + "\n-----END PRIVATE KEY-----";
        Files.write(Paths.get(PRIVATE_KEY_FILE), keyString.getBytes());
    }

    public static PrivateKey loadPrivateKey() throws Exception {
        String keyString = new String(Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE)));
        keyString = keyString.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");
        byte[] keyBytes = Base64.getMimeDecoder().decode(keyString);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR_ALGORITHM);
        return keyFactory.generatePrivate(spec);
    }

    public static PrivateKey getPrivateKey(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR_ALGORITHM);
        return keyFactory.generatePrivate(spec);
    }
}
