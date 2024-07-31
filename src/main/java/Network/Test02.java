package Network;

import static Network.FileTransferClient.decryptBinaryFile;
import static Network.FileTransferClient.encryptBinaryFile;

public class Test02 {
    public static void main(String[] args) throws Exception {
        encryptBinaryFile("img.png");
        decryptBinaryFile("img.png.encrypted");
    }
}
