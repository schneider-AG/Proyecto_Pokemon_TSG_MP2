package modelo;
public class Pocion extends Objeto {

    public Pocion() {
        super(
            "Pocion",
            "/Cartas_POCKETCG/Pocion.png",
            "Cura 20 PS al Pokemon activo propio",
            true
        );
    }

    @Override
    public Boolean puedeActivar(Jugador jugador) {
        return jugador != null
            && (jugador.getCampo().getPokemonActivo() != null
                || !jugador.getCampo().getBanco().isEmpty());
    }

    @Override
    public String Activar(Jugador jugador, Pokemon objetivo) {
        if(!puedeActivar(jugador)){
            return "No puedes activar " + getNombre() + " en este momento.";
        }
        boolean esPropio = (objetivo != null) &&
            (objetivo == jugador.getCampo().getPokemonActivo() || jugador.getCampo().getBanco().contains(objetivo));

        if (!esPropio) {
            return "Debes hacer clic sobre uno de tus propios Pokémon (Activo o Banca).";
        }
        if (objetivo.getPsActual() >= objetivo.getPsMaximo()) {
            return objetivo.getNombre() + " ya tiene todos sus PS al máximo.";
        }
        objetivo.curar(20);

        return null;
    }
}
