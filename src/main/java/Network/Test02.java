package Network;

import static Network.FileTransferClient.decryptBinaryFile;
import static Network.FileTransferClient.encryptBinaryFile;

public class Test02 {
    public static void main(String[] args) throws Exception {
        encryptBinaryFile("11MB.png");
        decryptBinaryFile("11MB.png.encrypted");
    }
}
