package vista;
import modelo.*;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            PantallaInicio inicio = new PantallaInicio();
            inicio.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            inicio.setVisible(true);
        });

}
}
