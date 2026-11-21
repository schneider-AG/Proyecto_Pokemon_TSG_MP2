package modelo;

public enum CondicionVictoria {
    PUNTOS_DE_PREMIO("acumuló 3 puntos de premio"),
    MAZO_VACIO("el rival se quedó sin cartas para robar"),
    SIN_POKEMON("el rival se quedó sin Pokémon en juego"),
    RENDICION("el rival se rindió");

    private final String descripcion;

    CondicionVictoria(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
}
