package modelo;

public interface Activable {

    Boolean puedeActivar(Jugador jugador);

    String Activar(Jugador jugador, Pokemon objetivo);
}