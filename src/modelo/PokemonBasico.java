package modelo;
public class PokemonBasico extends Pokemon{

    public PokemonBasico(String nombre, String rutaImagen, TipoEnergia tipo, short psMaximo, short psActual, 
        byte costoRetirada) {
        super(nombre, rutaImagen, tipo, psMaximo, psActual, costoRetirada);
    }

    @Override
    public byte getPuntosPremio() {
        return 1;
    }

    @Override
    public Boolean esPokemonBasico() {
        return true;
    }

        @Override
    public String getEtapa() {
        return "Basico";
    }

    
}
