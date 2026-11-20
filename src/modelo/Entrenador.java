package modelo;
public abstract class Entrenador extends Carta {

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

    public abstract Boolean puedeActivar(Jugador jugador);

    public abstract String Activar(Jugador jugador, Pokemon objetivo);

}