package modelo;
public class InvestigacionDelProfesor extends Soporte {

    public InvestigacionDelProfesor() {
        super(
            "Investigacion del Profesor",
            "/Cartas_POCKETCG/Investigacion_del_Profesor.png",
            "Descarta tu mano actual y roba 2 cartas del mazo",
            false
        );
    }

@Override
public Boolean puedeActivar(Jugador jugador) {
    return jugador != null &&
        jugador.puedeJugarSoporte() &&
        jugador.puedeRobar();
}

@Override
public String Activar(Jugador jugador, Pokemon objetivo) {

    if (!puedeActivar(jugador)) {
        return "No puedes activar " + getNombre() + " en este momento.";
    }
    jugador.robarCarta();
    jugador.robarCarta();
    jugador.registrarSoporte();

    return null;
}
}