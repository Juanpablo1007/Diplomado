package integrity;

import util.Util;

public class HasherTester2 {

    public static void main(String[] args) throws Exception {
        String filename = "C:\\Users\\JUAN PABLO\\Desktop\\diplomado\\laboratorioHash\\src\\ejemplo.pdf";
        String hash = Util.getHashFile(filename, "SHA-256");
        System.out.println(hash);
    }
}
