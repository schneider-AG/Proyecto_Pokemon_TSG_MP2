package modelo;
import java.util.ArrayList;
import java.util.List;

public abstract class Pokemon extends Carta{
    private TipoEnergia tipo;
    private short psMaximo, psActual;
    private byte costoRetirada;
    private List<Ataque> ataques;
    private List<TipoEnergia> energias;
    private int turnoEntrada;
    private int turnoUltimaEvolucion = -1;

    public Pokemon(String nombre, String rutaImagen, TipoEnergia tipo, short psMaximo, short psActual, byte costoRetirada) {
        super(nombre, rutaImagen);
        this.tipo = tipo;
        this.psMaximo = psMaximo;
        this.psActual = psActual;
        this.costoRetirada = costoRetirada;
        this.ataques = new ArrayList<>();
        this.energias = new ArrayList<>();
    }

    public void adjuntarEnergia(TipoEnergia energia){
        if(energia != null){
            this.energias.add(energia);
        }
    }

    public void agregarAtaque(Ataque ataque){
        if(ataque == null){
            return;
        }

        if(this.ataques.size() >= 2){
            return;
        }
        
        this.ataques.add(ataque);
    }

    public boolean puedeAtacar(Ataque ataque) {
        if (ataque == null || this.energias.isEmpty()) {
            return false;
        }

        TipoEnergia energiaRequerida = ataque.getTipoDeEnergia();

        if(energiaRequerida == TipoEnergia.INCOLORO){
            return energias.size() >= ataque.getCostoEnergia();
        }

        int cantidadEnergia = 0;

        for(TipoEnergia energia : this.energias){
            if(energia == energiaRequerida){
                cantidadEnergia ++;
            }
        }

        return cantidadEnergia >= ataque.getCostoEnergia();
        
    }

    public void atacar(Ataque ataque, Pokemon pokemonAtacado){
        if(ataque == null || pokemonAtacado == null){

            return;
        }

        if(!puedeAtacar(ataque)){
            return;
        }

        int danio = ataque.getDanioBase();

        if(pokemonAtacado.getTipo().esDebilContra(getTipo())){
            danio += 20;

        }

        pokemonAtacado.recibirDaño(danio);

    }

    public void recibirDaño(int cantidadDanio) {
        if(cantidadDanio <= 0) {
            return;
        }
        
        this.psActual -= cantidadDanio;

        if(this.psActual <= 0){
            this.psActual = 0;
        }

    }

    public void curar(int cantidadCuracion){
        if(cantidadCuracion <= 0){
            return;
        }

        this.psActual += cantidadCuracion;

        if(this.psActual >= this.psMaximo){
            this.psActual = this.psMaximo;
        }

    }

    public boolean estaDerrotado(){
        return this.psActual <= 0;
    }

    public TipoEnergia getTipo() {
        return tipo;
    }
    public short getPsMaximo() {
        return psMaximo;
    }
    public short getPsActual() {
        return psActual;
    }
    public byte getCostoRetirada() {
        return costoRetirada;
    }
    public List<Ataque> getAtaques() {
        return ataques;
    }
    public List<TipoEnergia> getEnergias() {
        return energias;
    }
    public void setCostoRetirada(byte costoRetirada) {
        this.costoRetirada = costoRetirada;
    }
    
    public abstract byte getPuntosPremio();

    public abstract String getEtapa();

    public void heredarEstado(Pokemon anterior) {
    heredarDe(anterior);
    this.turnoEntrada = anterior.turnoEntrada;
    }


    public void heredarDe(Pokemon anterior) {
        int danio = anterior.psMaximo - anterior.psActual;
        this.energias.addAll(anterior.energias);
        this.psActual = (short) Math.max(0, this.psMaximo - danio);
    }

    public int getTurnoEntrada() {
        return turnoEntrada;
    }

    public void setTurnoEntrada(int turnoEntrada) {
        this.turnoEntrada = turnoEntrada;
    }

    public int getTurnoUltimaEvolucion() {
        return turnoUltimaEvolucion;
    }

    public void setTurnoUltimaEvolucion(int turnoUltimaEvolucion) {
        this.turnoUltimaEvolucion = turnoUltimaEvolucion;
    }

}