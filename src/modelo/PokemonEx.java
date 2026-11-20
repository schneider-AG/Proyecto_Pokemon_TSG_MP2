package modelo;
public class PokemonEx extends Pokemon{

    public PokemonEx(String nombre, String rutaImagen, TipoEnergia tipo, short psMaximo, short psActual,
        byte costoRetirada) {
        super(nombre, rutaImagen, tipo, psMaximo, psActual, costoRetirada);
    }

    @Override
    public byte getPuntosPremio() {
        return 2;
    }

    @Override
    public Boolean esPokemonBasico() {
        return true; // los pokemon-ex tambien son basicos
    }

    @Override
    public String getEtapa() {
        return "Basico Ex";
    }

}
