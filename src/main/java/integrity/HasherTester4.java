package integrity;

import static integrity.Hasher.checkIntegrityFile;
import static integrity.Hasher.generateIntegrityCheckerFile;

public class HasherTester4 {



    public static void main(String[] args) throws Exception {
        String directoryPath = "C:\\Users\\JUAN PABLO\\Desktop\\diplomado\\laboratorioHash\\src\\Archivos";
        String outputFileName = "C:\\Users\\JUAN PABLO\\Desktop\\diplomado\\laboratorioHash\\src\\integrity.txt";

        System.out.println("Archivo de integridad generado exitosamente.");

        checkIntegrityFile(directoryPath, outputFileName);
    }
}
