package util;

import java.io.*;
import java.net.Socket;

public class Files {
    public static void sendObject(Object object, Socket socket) throws Exception {
        byte[] objectBA = Util.objectToByteArray(object);
        BufferedOutputStream toNetwork = new BufferedOutputStream(socket.getOutputStream());

        // Enviar el tamaño del objeto primero
        toNetwork.write(Util.intToByteArray(objectBA.length));
        toNetwork.flush();

        // Pausa para simular latencia de red
        pause(500);

        // Enviar el objeto en bytes
        toNetwork.write(objectBA);
        toNetwork.flush();
        pause(50);
    }

    public static Object receiveObject(Socket socket) throws Exception {
        BufferedInputStream fromNetwork = new BufferedInputStream(socket.getInputStream());

        // Leer el tamaño del objeto
        byte[] sizeBuffer = new byte[4];
        fromNetwork.read(sizeBuffer);
        int objectSize = Util.byteArrayToInt(sizeBuffer);

        // Leer el objeto en bytes
        byte[] objectBuffer = new byte[objectSize];
        fromNetwork.read(objectBuffer);

        // Convertir los bytes del objeto de nuevo a un objeto
        Object object = Util.byteArrayToObject(objectBuffer);
        return object;
    }

    public static String receiveFile(String folder, Socket socket) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedInputStream fromNetwork = new BufferedInputStream(socket.getInputStream());

        String filename = reader.readLine();
        filename = folder + File.separator + filename;

        BufferedOutputStream toFile = new BufferedOutputStream(new FileOutputStream(filename));

        System.out.println("File to receive: " + filename);

        String sizeString = reader.readLine();
        long size = Long.parseLong(sizeString.split(":")[1]);
        System.out.println("Size: " + size);

        byte[] blockToReceive = size > 1024 ? new byte[1024] : new byte[(int) size];
        int in;
        long remainder = size;
        while ((in = fromNetwork.read(blockToReceive)) != -1) {
            toFile.write(blockToReceive, 0, in);
            remainder -= in;
            if (remainder == 0) break;
        }

        pause(500);

        toFile.flush();
        toFile.close();
        System.out.println("File received: " + filename);

        return filename;
    }

    public static void sendFile(String filename, Socket socket) throws Exception {
        System.out.println("File to send: " + filename);
        File localFile = new File(filename);
        BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(localFile));

        long size = localFile.length();
        System.out.println("Size: " + size);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(localFile.getName());
        printWriter.println("Size:" + size);

        BufferedOutputStream toNetwork = new BufferedOutputStream(socket.getOutputStream());

        pause(1000);

        byte[] blockToSend = size > 1024 ? new byte[1024] : new byte[(int) size];
        int in;
        while ((in = fromFile.read(blockToSend)) != -1) {
            toNetwork.write(blockToSend, 0, in);
        }

        toNetwork.flush();
        fromFile.close();

        pause(500);
    }

    public static void pause(int milliseconds) throws Exception {
        Thread.sleep(milliseconds);
    }
}
