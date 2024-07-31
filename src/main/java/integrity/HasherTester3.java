package integrity;

import static integrity.Hasher.generateIntegrityCheckerFile;

public class HasherTester3 {

    public static void main(String[] args) throws Exception {
        String directoryPath = "C:\\Users\\JUAN PABLO\\Desktop\\diplomado\\laboratorioHash\\src\\Archivos";
        String outputFileName = "C:\\Users\\JUAN PABLO\\Desktop\\diplomado\\laboratorioHash\\src\\integrity.txt";
        generateIntegrityCheckerFile(directoryPath, outputFileName);
        System.out.println("Archivo de integridad generado exitosamente.");
    }
}
