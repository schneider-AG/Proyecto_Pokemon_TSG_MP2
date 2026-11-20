package modelo;
public abstract class Carta {
    
    private String nombre;
    private String rutaImagen;

    public Carta(String nombre, String rutaImagen) {
        this.nombre = nombre;
        this.rutaImagen = rutaImagen;
    }

    public Boolean esPokemonBasico() {
        return false;
    }
    public String getNombre() {
        return nombre;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

}