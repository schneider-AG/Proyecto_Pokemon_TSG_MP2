package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PantallaCambio extends JDialog {

    private Image imagenFondo;
    private JLabel lblTurno;
    private JLabel lblContinuar;

    public PantallaCambio(JFrame padre, String nombreJugador, String rutaImagen) {
        super(padre, true);
        setUndecorated(true);
        setAlwaysOnTop(true);

        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, pantalla.width, pantalla.height);

        cargarImagen(rutaImagen);
        inicializarComponentes(nombreJugador);
        configurarTeclado();
    }

    private void cargarImagen(String ruta) {
        if (ruta != null) {
            URL url = getClass().getResource(ruta);
            if (url != null) {
                imagenFondo = new ImageIcon(url).getImage();
            }
        }
    }

    private void inicializarComponentes(String nombreJugador) {
        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        lblTurno = new JLabel("TURNO DEL JUGADOR " + nombreJugador.toUpperCase(), SwingConstants.CENTER);
        lblTurno.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTurno.setForeground(Color.WHITE);
        lblTurno.setOpaque(true);
        lblTurno.setBackground(new Color(10, 15, 40, 185));
        lblTurno.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        lblContinuar = new JLabel("Presiona cualquier tecla para continuar", SwingConstants.CENTER);
        lblContinuar.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblContinuar.setForeground(new Color(255, 235, 130));
        lblContinuar.setOpaque(true);
        lblContinuar.setBackground(new Color(10, 15, 40, 195));
        lblContinuar.setBorder(BorderFactory.createEmptyBorder(16, 10, 18, 10));

        panelFondo.add(lblTurno, BorderLayout.NORTH);
        panelFondo.add(lblContinuar, BorderLayout.SOUTH);
        setContentPane(panelFondo);
    }

    private void configurarTeclado() {
        setFocusable(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                requestFocusInWindow();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                dispose();
            }
        });
    }
}