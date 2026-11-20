
package modelo;
import java.util.ArrayList;
import java.util.List;

public class MotorGrafico {

    private Jugador jugador1;
    private Jugador jugador2;

    private Consola consola;

    private int turno;
    private int turnoGlobal = 1;


    private boolean partidaTerminada = false;
    private Jugador ganador = null;


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
            anunciarGanador();
            return "FIN_PARTIDA";
        }

        prepararTurno();
        return null;
    }

    public void prepararTurno() {
        Jugador actual = jugadorActual();
        actual.iniciarTurno();
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
            anunciarGanador();
            return "FIN_PARTIDA";
        }
        prepararTurno();
        return null;
    }

private void evolucionarPokemon(Jugador jugador) {
    List<Carta> evoluciones = new ArrayList<>();
    for (Carta carta : jugador.getMano()) {
        if (carta instanceof PokemonEvolucion) evoluciones.add(carta);
    }
    if (evoluciones.isEmpty()) {
        System.out.println("No tienes cartas de evolucion en la mano.");
        return;
    }
    for (int i = 0; i < evoluciones.size(); i++) {
        System.out.println((i + 1) + ". " + evoluciones.get(i).getNombre());
    }
    int opcion = consola.elegirEntrenador();
    if (opcion < 1 || opcion > evoluciones.size()) {
        System.out.println("Seleccion invalida.");
        return;
    }
    Carta carta = evoluciones.get(opcion - 1);
    List<Pokemon> propios = new ArrayList<>();
    Pokemon activo = jugador.getCampo().getPokemonActivo();
    if (activo != null) propios.add(activo);
    propios.addAll(jugador.getCampo().getBanco());
    if (propios.isEmpty()) {
        System.out.println("No tienes Pokemon en juego.");
        return;
    }
    for (int i = 0; i < propios.size(); i++) {
        Pokemon pokemon = propios.get(i);
        System.out.println((i + 1) + ". " + pokemon.getNombre()
            + " (" + pokemon.getPsActual() + "/" + pokemon.getPsMaximo() + " PS)");
    }
    int opcionPokemon = consola.elegirPokemon();
    if (opcionPokemon < 1 || opcionPokemon > propios.size()) {
        System.out.println("Seleccion invalida.");
        return;
    }
    Pokemon objetivo = propios.get(opcionPokemon - 1);
    String motivo = jugador.motivoNoEvolucion(objetivo, carta);
    if (motivo != null) {
        System.out.println(motivo);
    } else if (jugador.evolucionar(carta, objetivo)) {
        System.out.println(objetivo.getNombre() + " evoluciono a " + carta.getNombre() + ".");
    }
}

public Jugador obtenerGanador() {
    if (jugador1.gano() || !jugador2.tienePokemonEnJuego()) {
        return jugador1;
    }
    if (jugador2.gano() || !jugador1.tienePokemonEnJuego()) {
        return jugador2;
    }
    return null; // La partida sigue
}

public boolean verificarGanador() {
    return obtenerGanador() != null;
}

    public String anunciarGanador() {

        if (!jugador1.tienePokemonEnJuego() && !jugador2.tienePokemonEnJuego()) {
            return "La partida no tiene ganador: ninguno de los jugadores tiene Pokemon en juego.";
        }

        if (jugador1.gano() ||
            !jugador2.tienePokemonEnJuego()) {

            return jugador1.getNombre() + " ha ganado la partida.";
        }

        return jugador2.getNombre() + " ha ganado la partida";
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
    this.ganador = jugadorRival();
    this.partidaTerminada = true;
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
