package analisis_desempeño_DES_RSA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    public static File createFile(String fileName, int size) throws IOException {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] data = new byte[size];
            fos.write(data);
        }
        return file;
    }

    public static void appendToCsv(File csvFile, String data) throws IOException {
        try (FileWriter writer = new FileWriter(csvFile, true)) {
            writer.append(data).append("\n");
        }
    }

    public static void createCsvWithHeader(File csvFile, String header) throws IOException {
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.append(header).append("\n");
        }
    }
}
