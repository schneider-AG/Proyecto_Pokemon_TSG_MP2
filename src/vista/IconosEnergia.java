package vista;
import modelo.TipoEnergia;

public class IconosEnergia {

    public String getRutaEnergia(TipoEnergia energia){
        switch(energia){
            case FUEGO:
                return "/Cartas_POCKETCG/EnergiaFuego.png";
            case AGUA:
                return "/Cartas_POCKETCG/EnergiaAgua.png";
            case ELECTRICO:
                return "/Cartas_POCKETCG/EnergiaElectrica.png";
            case PSIQUICO:
                return "/Cartas_POCKETCG/EnergiaPsiquica.png";
            default:
                return null;
        }
        }
    }
