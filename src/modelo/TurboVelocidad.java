package modelo;
public class TurboVelocidad extends Objeto {

    public TurboVelocidad() {
        super(
            "Turbo Velocidad",
            "/Cartas_POCKETCG/Turbo_Velocidad.png",
            "Reduce en 1 el costo de retirada del Pokemon activo",
            false
        );
    }

    @Override
    public Boolean puedeActivar(Jugador jugador) {

        return jugador != null &&
            jugador.getCampo().getPokemonActivo() != null &&
            jugador.puedeUsarTurboVelocidad();
    }

    @Override
    public String Activar(Jugador jugador, Pokemon objetivo) {

        if (!puedeActivar(jugador)) {
            return "No puedes activar " + getNombre() + " en este momento.";
        }

        Pokemon activo = jugador.getCampo().getPokemonActivo();
        jugador.activarTurboVelocidad();

        return null;
    }
}
