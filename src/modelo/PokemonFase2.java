package modelo;

public class PokemonFase2 extends PokemonEvolucion{

    public PokemonFase2(String nombre, TipoEnergia tipo, short psMaximo, short psActual, byte costoRetirada,
            String evolucionaDe) {
        super(nombre, tipo, psMaximo, psActual, costoRetirada, evolucionaDe);
    }

    @Override
    public String getEtapa() {
        return "Fase 2";
        
    }

    
}
