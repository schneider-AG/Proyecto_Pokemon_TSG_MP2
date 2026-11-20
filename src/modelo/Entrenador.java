package modelo;
public abstract class Entrenador extends Carta implements Activable {

    private String descripcion;
    private boolean requiereObjetivoTablero;

    public Entrenador(String nombre, String rutaImagen, String descripcion, boolean requiereObjetivoTablero) {
        super(nombre, rutaImagen);
        this.descripcion = descripcion;
        this.requiereObjetivoTablero = requiereObjetivoTablero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean requiereObjetivoTablero() {
        return requiereObjetivoTablero;
    }

}