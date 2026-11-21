package vista;

import javax.swing.SwingUtilities;

/** Punto de entrada de la aplicación. */
public class App {

    private App() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PantallaInicio().setVisible(true));
    }
}
