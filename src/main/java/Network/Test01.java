package Network;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static Network.FileTransferClient.decryptTextFile;
import static Network.FileTransferClient.encryptTextFile;

public class Test01 {

    public static void main(String[] args) throws Exception {

        decryptTextFile(encryptTextFile("test.txt"));
    }
}
