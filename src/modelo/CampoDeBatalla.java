package modelo;
import java.util.ArrayList;
import java.util.List;

public class CampoDeBatalla {
    
    private Pokemon pokemonActivo;
    private List<Pokemon> banco;

    public CampoDeBatalla(){
        this.pokemonActivo = null;
        this.banco = new ArrayList<>();
    }

    public void colocarActivo(Pokemon pokemon) {
        if(pokemon != null){
        this.pokemonActivo = pokemon;
        }
    }

    public Pokemon getPokemonActivo() {
        return pokemonActivo;
    }

    public List<Pokemon> getBanco() {
        return banco;
    }

    public boolean agregarABanco(Pokemon pokemon) {
        if( pokemon != null && banco.size() < 3){
            banco.add(pokemon);
            return true;
        }
        return false;
    }

    public void retirarActivo() {
        this.pokemonActivo = null;
    }

    public boolean hayPokemonEnJuego() {
        return this.pokemonActivo != null;
    }

    public void reemplazar(Pokemon viejo, Pokemon nuevo) {
    if (pokemonActivo == viejo) {
        pokemonActivo = nuevo;
        return;
    }
    int i = banco.indexOf(viejo);
    if (i >= 0){
        banco.set(i, nuevo);
    }
    
}

}
