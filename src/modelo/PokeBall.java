package modelo;
import java.util.List;

public class PokeBall extends Objeto {

    public PokeBall() {
        super(
            "Poke Ball",
            "/Cartas_POCKETCG/Poke_Ball.png",
            "Busca un Pokemon basico y lo agrega a la mano",
            false
        );
    }

    @Override
    public Boolean puedeActivar(Jugador jugador) {
        return jugador != null;
    }

@Override
public String Activar(Jugador jugador, Pokemon objetivo) {

    if (!puedeActivar(jugador)) {
        return "No puedes activar " + getNombre() + " en este momento.";
    }

    List<Carta> cartas =
        jugador.getMazo().getCartas();

    for (int i = 0; i < cartas.size(); i++) {

        Carta carta = cartas.get(i);

        if (carta.esPokemonBasico()) {

            jugador.getMano().add(carta);

            cartas.remove(i);

            jugador.getMazo().barajar();

            return null;
        }
    }

    return "No se encontro una carta basica.";
}
}