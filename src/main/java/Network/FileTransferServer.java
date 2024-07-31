package Network;

import Persintencia.Usuario;
import util.Files;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class FileTransferServer {
    public static final int PORT = 4000;

    private ServerSocket listener;
    private Socket serverSideSocket;
    private PrintWriter toNetwork;
    static HashMap<String,Double> usuarios = new HashMap<>();

    private int port;

    public FileTransferServer() {
        System.out.println("Carlos E. Gomez - May 20/2024");
        System.out.println("File transfer server is running on port: " + this.port);
    }

    public FileTransferServer(int port) {
        this.port = port;
        System.out.println("Carlos E. Gomez - May 20/2024");
        System.out.println("File transfer server is running on port: " + this.port);
    }

    private void init() throws Exception {
        listener = new ServerSocket(PORT);

        while (true) {
            serverSideSocket = listener.accept();

            protocol(serverSideSocket);
        }
    }

    public void protocol(Socket socket) throws Exception {
        //Files.receiveFile("Docs", socket);
        //Files.sendFile("2.jpg", socket);

        Usuario usuario = (Usuario) Files.receiveObject( socket);
        String fromUser = realizarTranssacion(usuario);

        Files.sendObject(fromUser, socket);

    }

    public static void main(String[] args) throws Exception{

        FileTransferServer fts = null;
        if (args.length == 0) {
            fts = new FileTransferServer();
        } else {
            int port = Integer.parseInt(args[0]);
            fts = new FileTransferServer(port);
        }
        fts.init();


    }
    public static String realizarTranssacion(Usuario usuario){
        if(usuarios.containsKey(usuario.getNombre())){
            Double moonto = usuarios.get(usuario.getNombre());
            moonto+=usuario.getMonto();
            usuarios.put(usuario.getNombre(), moonto);
            return "Transsación realziada . saldo: "+ moonto;
        }
        usuarios.put(usuario.getNombre(), usuario.getMonto());
        return "Cuenta creada exitosamente :) . saldo:  "+ usuario.getMonto();
    }
}