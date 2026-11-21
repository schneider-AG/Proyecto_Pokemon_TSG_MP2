
package modelo;
import java.util.ArrayList;
import java.util.List;

public class MotorGrafico {

    private Jugador jugador1;
    private Jugador jugador2;

    private int turno;
    private int turnoGlobal = 1;


    private boolean partidaTerminada = false;
    private Jugador ganador = null;
    private CondicionVictoria condicionVictoria = null;


    public MotorGrafico(
        Jugador jugador1,
        Jugador jugador2
    ) {

        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.turno = 1;
    }

public boolean iniciarPartida() {
    this.turno = 1;

    jugador1.robarManoInicial();
    jugador2.robarManoInicial();

    jugador1.iniciarTurno(false, this.turno);
    return true;
}


    public List<Carta> getBasicosEnMano(Jugador jugador) {

        List<Carta> mano = jugador.getMano();

        // Recolectar los Pokémon básicos de la mano
        List<Carta> basicos = new ArrayList<>();
        for (Carta carta : mano) {
            if (carta instanceof Pokemon && carta.esPokemonBasico()) {
                basicos.add(carta);
            }
        }

        return basicos;

    }


    public void ejecutarPartida() {

        while (!verificarGanador()) {

            ejecutarTurno(
                jugadorActual(),
                jugadorRival()
            );

            cambiarTurno();
        }

        anunciarGanador();
    }

    public String pasarTurno(){
        cambiarTurno();
        jugadorActual().iniciarTurno();
        if (jugadorActual().getCampo().getPokemonActivo() == null) {
            return "Debes tener un Pokémon Activo antes de pasar el turno.";
        }

        if (verificarGanador()) {
            return "FIN_PARTIDA";
        }

        prepararTurno();
        if (verificarGanador()) {
            return "FIN_PARTIDA";
        }
        return null;
    }

    public void prepararTurno() {
        Jugador actual = jugadorActual();
        actual.iniciarTurno();
        if (!actual.puedeRobar()) {
            registrarVictoria(jugadorRival(), CondicionVictoria.MAZO_VACIO);
            return;
        }
        actual.robarCarta();
        actual.generarEnergiaTurno(this.turno);
    }

    public boolean esPrimerTurno(){
        return turno <=2 ;
    }


private void ejecutarTurno(
    Jugador jugadorActual,
    Jugador rival
){

    jugadorActual.iniciarTurno();

    jugadorActual.robarCarta();

    jugadorActual.generarEnergiaTurno(this.turno);

    boolean terminarTurno = false;
}


    public String adjuntarEnergia(Pokemon objetivo){
        if(partidaTerminada){
            return "La partida ya termino.";
        }
        return jugadorActual().adjuntarEnergia(objetivo);
    }

public String jugarEntrenador(Entrenador entrenador, Pokemon objetivo) {
    return jugadorActual().jugarEntrenador(entrenador, objetivo);
}
    public boolean colocarPokemonEnBanca(Carta carta){
        if (carta instanceof Pokemon &&
            carta.esPokemonBasico()) {
                return jugadorActual().colocarPokemonEnJuego(carta);
                }
        return false;
        }

    public boolean retirarActivo(Pokemon nuevoActivo){
        return jugadorActual().retirarPokemonActivo(nuevoActivo);
    }

    // 1. Validar antes de pedirle el clic al usuario (cumple RF9 de explicar acciones inválidas)
public String validarRetirada() {
    if (jugadorActual().isYaRetiroEsteTurno()) {
        return "Ya retiraste un Pokémon en este turno.";
    }
    Pokemon activo = jugadorActual().getCampo().getPokemonActivo();
    if (activo == null) {
        return "No tienes un Pokémon activo.";
    }
    if (jugadorActual().getCampo().getBanco().isEmpty()) {
        return "No tienes Pokémon en la banca para realizar el cambio.";
    }
    int costo = activo.getCostoRetirada(); // Usa el getter de retirada de tu clase Pokemon
    if (activo.getEnergias().size() < costo) {
        return activo.getNombre() + " necesita " + costo + " energía(s) para retirarse.";
    }
    return null; // null = cumple todas las condiciones
}

// 2. Descartar energías e intercambiar Activo <-> Banca en el modelo
public String retirar(Pokemon elegidoBanca) {
    CampoDeBatalla campoAux = jugadorActual().getCampo() ;
    String error = validarRetirada();
    if (error != null) {
        return error;
    }
    if (elegidoBanca == null || !campoAux.getBanco().contains(elegidoBanca)) {
        return "Debes elegir un Pokémon válido de tu banca.";
    }

    Pokemon activoAnterior = campoAux.getPokemonActivo();
    int costo = activoAnterior.getCostoRetirada();

    // Descartar la cantidad de energías del costo de retirada
    for (int i = 0; i < costo; i++) {
        activoAnterior.getEnergias().remove(0);
    }

    // Intercambiar posiciones en Campo (el activo pasa a la banca conservando daño y energías restantes)
    campoAux.getBanco().remove(elegidoBanca);
    campoAux.getBanco().add(activoAnterior);
    campoAux.colocarActivo(elegidoBanca); // Usa tu setter de activo en Campo

    jugadorActual().setYaRetiroEsteTurno(true);
    return null;
}

    public String atacar(int indiceAtaque){
        List<Ataque> ataques = jugadorActual().getCampo().getPokemonActivo().getAtaques();
        boolean resultado = jugadorActual().atacar(ataques.get(indiceAtaque), jugadorRival());
        if(turno <= 1){
            return "No se puede atacar en el primer turno.";
        }
        if (!resultado) {
            return "No fue posible atacar (revisa tus energías).";
        }

        if (verificarGanador()) {
            return "FIN_PARTIDA";
        }
        return null;
    }
    

public Jugador obtenerGanador() {
    if (ganador != null) {
        return ganador;
    }
    if (jugador1.gano()) {
        registrarVictoria(jugador1, CondicionVictoria.PUNTOS_DE_PREMIO);
    } else if (jugador2.gano()) {
        registrarVictoria(jugador2, CondicionVictoria.PUNTOS_DE_PREMIO);
    } else if (!jugador1.tienePokemonEnJuego()) {
        registrarVictoria(jugador2, CondicionVictoria.SIN_POKEMON);
    } else if (!jugador2.tienePokemonEnJuego()) {
        registrarVictoria(jugador1, CondicionVictoria.SIN_POKEMON);
    }
    return ganador;
}

public boolean verificarGanador() {
    return obtenerGanador() != null;
}

    public String anunciarGanador() {
        Jugador ganadorActual = obtenerGanador();
        if (ganadorActual == null || condicionVictoria == null) {
            return "La partida continúa.";
        }
        return "¡" + ganadorActual.getNombre() + " es el campeón del duelo!\n"
            + "Condición de victoria: " + condicionVictoria.getDescripcion() + ".";
    }

    private void registrarVictoria(Jugador ganador, CondicionVictoria condicion) {
        this.ganador = ganador;
        this.condicionVictoria = condicion;
        this.partidaTerminada = true;
    }

    public CondicionVictoria getCondicionVictoria() {
        return condicionVictoria;
    }

public String seleccionarCartaMano(Carta carta) {
    Jugador actual = jugadorActual();
    if (carta == null || !actual.getMano().contains(carta)) {
        return "Esa carta no está en tu mano";
    }
    // Si es Básico, valida de inmediato si cabe en el campo
    if (carta instanceof PokemonBasico) {
        return actual.validarColocacion(carta);
    }
    // Si es Evolución, permite seleccionarla para elegir el Pokémon objetivo en el segundo clic
    return null;
}

public String jugarCartaEnCampo(Carta carta, boolean esPuestoActivo, Pokemon objetivo) {
        Jugador actual = jugadorActual();

        // 1. Si es Evolución, requiere hacer clic sobre un Pokémon ya en juego
        if (carta instanceof PokemonEvolucion) {
            String error = actual.validarEvolucion(carta, objetivo);
            if (error != null) {
                return error;
            }
            actual.evolucionar(carta, objetivo);
            return null;
        }

        // 2. Si es Pokémon Básico, requiere hacer clic en un espacio vacío
        if (objetivo != null) {
            return "Ese espacio ya está ocupado";
        }
        if (esPuestoActivo && actual.getCampo().getPokemonActivo() != null) {
            return "Ya tienes un Pokémon activo";
        }
        if (!esPuestoActivo && actual.getCampo().getPokemonActivo() == null) {
            return "Primero coloca un Pokémon en el puesto Activo";
        }

        String error = actual.validarColocacion(carta);
        if (error != null) {
            return error;
        }
        actual.colocarPokemonEnJuego(carta);
        return null;
}

public Jugador rendirse() {
    if (partidaTerminada) {
        return ganador;
    }
    registrarVictoria(jugadorRival(), CondicionVictoria.RENDICION);
    return this.ganador;
}

public boolean isPartidaTerminada() {
    return partidaTerminada;
}

public Jugador getGanador() {
    return ganador;
}

private void cambiarTurno() {
    this.turno++;
}

public Jugador jugadorActual() {

    if (turno % 2 != 0) {
        return jugador1;
    }

    return jugador2;
}

public Jugador jugadorRival() {

    if ((turno % 2) != 0) {
        return jugador2;
    }

    return jugador1;
}

public Jugador getJugador1() {
    return jugador1;
}

public Jugador getJugador2() {
    return jugador2;
}

public int getTurno() {
    return turno;
}

}
