package vista;
import modelo.TipoEnergia;

public class IconosEnergia {

    public String getRutaEnergia(TipoEnergia energia){
        switch(energia){
            case FUEGO:
                return "Media/EnergiaFuego.png";
            case AGUA:
                return "Media/EnergiaAgua.png";
            case ELECTRICO:
                return "Media/EnergiaElectrica.png";
            case PSIQUICO:
                return "Media/EnergiaPsiquica.png";

            default:
                System.out.println("Retorno nulo");
                return null;
        }
        }
    }
