package modelo;
public class TurboVelocidad extends Objeto {

    public TurboVelocidad() {
        super(
            "Turbo Velocidad",
            "Reduce en 1 el costo de retirada del Pokemon activo"
        );
    }

    @Override
    public Boolean puedeActivar(Jugador jugador) {

        return jugador != null &&
               jugador.getCampo().getPokemonActivo() != null &&
               jugador.puedeUsarTurboVelocidad();
    }

    @Override
    public Void Activar(Jugador jugador) {

        if (!puedeActivar(jugador)) {
            return null;
        }

        Pokemon activo = jugador.getCampo().getPokemonActivo();
        jugador.activarTurboVelocidad();
        System.out.println("Turbo Velocidad redujo en 1 la retirada de " + activo.getNombre()
            + " durante este turno.");

        return null;
    }
}
