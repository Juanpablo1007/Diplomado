package util;

import java.io.*;
import java.security.*;

public class Util {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    /**
     * Genera una cadena aleatoria de la longitud especificada.
     *
     * @param length La longitud de la cadena aleatoria a generar.
     * @return Una cadena aleatoria de la longitud especificada.
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // Selecciona un carácter aleatorio de la cadena CHARACTERS y lo añade a StringBuilder.
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }


    /**
     * Divide un array de bytes en fragmentos de un tamaño especificado.
     *
     * @param input El array de bytes a dividir.
     * @param chunkSize El tamaño de cada fragmento.
     * @return Un array de arrays de bytes, donde cada subarray es un fragmento del tamaño especificado.
     */
    public static byte[][] split(byte[] input, int chunkSize) {
        // Calcula el número de fragmentos necesarios.
        int numberOfChunks = (int) Math.ceil((double) input.length / chunkSize);
        byte[][] output = new byte[numberOfChunks][];

        for (int i = 0; i < numberOfChunks; i++) {
            // Determina el índice de inicio y la longitud del fragmento actual.
            int start = i * chunkSize;
            int length = Math.min(input.length - start, chunkSize);

            // Crea un nuevo array de bytes para el fragmento y copia los datos correspondientes.
            byte[] temp = new byte[length];
            System.arraycopy(input, start, temp, 0, length);
            output[i] = temp;
        }

        return output;
    }


    /**
     * Une fragmentos de bytes en un solo array de bytes.
     *
     * @param input Un array de arrays de bytes, donde cada subarray es un fragmento.
     * @return Un array de bytes que es la unión de todos los fragmentos.
     */
    public static byte[] join(byte[][] input) {
        // Calcula la longitud total del array de salida.
        int totalLength = 0;
        for (byte[] array : input) {
            totalLength += array.length;
        }

        // Crea un nuevo array de bytes con la longitud total.
        byte[] output = new byte[totalLength];
        int currentPosition = 0;
        for (byte[] array : input) {
            // Copia cada fragmento en la posición correcta en el array de salida.
            System.arraycopy(array, 0, output, currentPosition, array.length);
            currentPosition += array.length;
        }

        return output;
    }


    public static String byteArrayToHexString(byte[] bytes, String separator) {
        String result = "";
        int cont = 0;
        for (int i = 0; i < bytes.length; i++) {
            result += String.format("%02x", bytes[i]) + separator;
            if (cont == 10){
                result += "\n";
                cont = -1;
            }
            cont++;
        }
        return result.toString();
    }

    public static void saveObject(Object o, String fileName) throws IOException {
        FileOutputStream fileOut;
        ObjectOutputStream out;

        fileOut = new FileOutputStream(fileName);
        out = new ObjectOutputStream(fileOut);

        out.writeObject(o);
        out.flush();
        out.close();
    }
    public static Object loadObject(String fileName) throws
            IOException,
            ClassNotFoundException,
            InterruptedException {
        FileInputStream fileIn;
        ObjectInputStream in;

        fileIn = new FileInputStream(fileName);
        in = new ObjectInputStream(fileIn);

      ///  Thread.sleep(100);

        Object o = in.readObject();

        fileIn.close();
        in.close();

        return o;
    }
    public static byte[] objectToByteArray(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(o);
        out.close();
        byte[] buffer = bos.toByteArray();

        return buffer;
    }
    public static Object byteArrayToObject(byte[] byteArray) throws
            IOException,
            ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteArray));
        Object o = in.readObject();
        in.close();
        return o;
    }

    public static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value
        };
    }
    public static int byteArrayToInt(byte[] byteArray) {
        return byteArray[0] << 24 | (byteArray[1] & 0xFF) << 16 | (byteArray[2] & 0xFF) << 8 | (byteArray[3] & 0xFF);
    }

    public static String getHashFile(String filename, String algorithm) throws Exception {
        MessageDigest hasher = MessageDigest.getInstance(algorithm);
        FileInputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        int in;
        while ((in = fis.read(buffer)) != -1) {
            hasher.update(buffer, 0, in);
        }
        fis.close();
        return Util.byteArrayToHexString(hasher.digest(), "");
    }
    public static void printKeyPair(KeyPair keyPair) {
        System.out.println(obtenerLlave(TiposLlavesEmun.PUBLIC_KEY,keyPair.getPublic()));
        System.out.println("\n \n");
        System.out.println(obtenerLlave(TiposLlavesEmun.PRIVATE_KEY,keyPair.getPrivate()));
    }
private static String obtenerLlave (TiposLlavesEmun tiposLlavesEmun, Key encoder  ){
    String keyEncoded = Base64.getEncoder().encodeToString(encoder.getEncoded());
    return  "-----BEGIN "+tiposLlavesEmun.getName()+"-----\n" +
                AcomodarFormato(keyEncoded) + "\n"
              +  "-----END "+ tiposLlavesEmun.getName()+"-----";
    }
    private static String AcomodarFormato(String texto){
        String resultado = "";
        int longitud = 64;
        int i = 0;
        while ( (i + longitud) < texto.length() ){
            resultado += texto.substring(i, i+longitud) + "\n";
            i += longitud;
            if ((i + longitud) > texto.length()){
                resultado += texto.substring(i, texto.length());
            }
        }
        //System.out.println(resultado);
        return resultado;
    }
    public static Base64.Encoder getEncoder() {
        return Base64.getEncoder();
    }

}
