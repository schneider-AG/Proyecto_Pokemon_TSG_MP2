package modelo;
public class Misty extends Soporte {

    public Misty() {
        super(
            "Misty",
            "/Cartas_POCKETCG/Misty.png",
            "Adjunta hasta 3 energias Agua a un Pokemon de Agua",
            true
        );
    }

@Override
public Boolean puedeActivar(Jugador jugador) {

    if (jugador == null) {
        return false;
    }

    if (!jugador.puedeJugarSoporte()) {
        return false;
    }
    
    return true;
}

@Override
public String Activar(Jugador jugador, Pokemon objetivo) {
    if (!puedeActivar(jugador)) {
        return "No puedes activar " + getNombre() + " en este momento.";
    }
    boolean esPropio = (objetivo != null) &&
            (objetivo == jugador.getCampo().getPokemonActivo() || jugador.getCampo().getBanco().contains(objetivo));

        if (!esPropio) {
            return "Debes hacer clic sobre uno de tus propios Pokémon (Activo o Banca).";
        }

    if (objetivo == null) {
        return "Selecciona un Pokémon de Agua para usar Misty.";
    }

    if (objetivo.getTipo() == TipoEnergia.AGUA) {

        objetivo.adjuntarEnergia(TipoEnergia.AGUA);
        objetivo.adjuntarEnergia(TipoEnergia.AGUA);
        objetivo.adjuntarEnergia(TipoEnergia.AGUA);
        jugador.registrarSoporte();

        return null;
        } else {
            return objetivo.getNombre() + " no es de tipo agua";
        }  
    }
}
