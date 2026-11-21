package modelo;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PanelFondo extends JPanel {
    private Image fondo;

    public PanelFondo(String ruta) {
        java.net.URL url = getClass().getResource(ruta);
        if (url != null) {
            fondo = new ImageIcon(url).getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondo == null) return;
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), null);
    }
}