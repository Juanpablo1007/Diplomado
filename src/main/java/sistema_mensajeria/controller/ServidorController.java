package sistema_mensajeria.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sistema_mensajeria.model.MensajeRespuesta.*;

public class ServidorController {
    private Map<String, String> usuario;
    private Map<String, ArrayList<String>> buzon;

    public ServidorController() {
        usuario = new HashMap<>();
        buzon = new HashMap<>();
    }

    public String registrar(String user, String publicKey){
        if (usuario.containsKey(user)){
            return String.format(USUARIO_YA_REGISTRADO, user);
        }
        usuario.put(user, publicKey);
        buzon.put(user, new ArrayList<>());
        return String.format(REGISTRAR_OK, user);
    }

    public String getLlave(String user){
        if (!usuario.containsKey(user)){
            return String.format(USUARIO_NO_REGISTRADO, user);
        }
        String llave = usuario.get(user);
        return String.format(LLAVE_OK, user, llave);
    }

    public String enviar(String user, String message){
        if (!usuario.containsKey(user)){
            return String.format(USUARIO_NO_REGISTRADO, user);
        }
        ArrayList<String> listMensajes = buzon.get(user);
        listMensajes.add(message);
        buzon.put(user, listMensajes);
        return String.format(MENSAJE_GUARDADO, user);
    }

    public String leer (String user){
        if (!usuario.containsKey(user)){
            return String.format(USUARIO_NO_REGISTRADO, user);
        }
        return String.format(CANTIDAD_MENSAJES, user, buzon.get(user).size());
    }

    public List<String> getBuzon(String user){
        return buzon.get(user);
    }
}
