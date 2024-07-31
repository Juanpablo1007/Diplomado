package util;

public enum TiposLlavesEmun {
    PUBLIC_KEY("PUBLIC KEY"),
    PRIVATE_KEY("PRIVATE KEY"),
    MESSAGE("MESSAGE");
    private String name;

    TiposLlavesEmun(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
