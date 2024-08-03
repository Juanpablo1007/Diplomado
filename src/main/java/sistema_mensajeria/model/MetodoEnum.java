package sistema_mensajeria.model;

public enum MetodoEnum {
    REGISTRAR("REGISTRAR"),
    OBTENER_LLAVE_PUBLICA("OBTENER_LLAVE_PUBLICA"),
    ENVIAR("ENVIAR"),
    LEER("LEER");

    private String value;

    MetodoEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
