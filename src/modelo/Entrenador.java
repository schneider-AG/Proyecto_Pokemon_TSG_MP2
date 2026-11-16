package modelo;
public abstract class Entrenador extends Carta implements Activable {

    private String descripcion;

    public Entrenador(String nombre, String descripcion) {
        super(nombre);
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}