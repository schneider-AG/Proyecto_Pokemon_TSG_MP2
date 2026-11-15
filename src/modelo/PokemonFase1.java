package modelo;

public class PokemonFase1 extends PokemonEvolucion{

    public PokemonFase1(String nombre, TipoEnergia tipo, short psMaximo, short psActual, byte costoRetirada,
            String evolucionaDe) {
        super(nombre, tipo, psMaximo, psActual, costoRetirada, evolucionaDe);
    }

    @Override
    public String getEtapa() {
        return "Fase 1";
    }
    
}
