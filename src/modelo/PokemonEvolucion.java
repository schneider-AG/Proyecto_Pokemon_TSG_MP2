package modelo;

public abstract class PokemonEvolucion extends Pokemon {

    private String evolucionaDe;

    public PokemonEvolucion(String nombre, String rutaImagen, TipoEnergia tipo, short psMaximo, short psActual, byte costoRetirada, String evolucionaDe) {
        super(nombre, rutaImagen, tipo, psMaximo, psActual, costoRetirada);
        this.evolucionaDe = evolucionaDe;
    }

    public String getEvolucionaDe() {
        return evolucionaDe;
    }

    public boolean puedeEvolucionar(Pokemon pokemon){
        return pokemon != null && pokemon.getNombre().equals(evolucionaDe);
    }

    @Override
    public byte getPuntosPremio() {
        return 1;
    }
    
    
}
