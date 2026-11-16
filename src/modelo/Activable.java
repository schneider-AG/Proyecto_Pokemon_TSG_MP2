package modelo;

/**
 * Capacidad de una carta que tiene un efecto activable durante el turno.
 */
public interface Activable {

    Boolean puedeActivar(Jugador jugador);

    Void Activar(Jugador jugador);
}