package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import modelo.CatalogoCartas;
import modelo.Jugador;
import modelo.Mazo;
import modelo.MotorGrafico;
import modelo.ZonaEnergia;


public class ConfiguracionPartida extends JFrame {

    private static final Color FONDO = new Color(241, 245, 249);
    private static final Color TINTA = new Color(30, 41, 59);
    private static final Color SECUNDARIO = new Color(100, 116, 139);
    private static final Color ACENTO = new Color(37, 99, 235);

    private static final String[] MAZOS = {
        "Selecciona un mazo",
        "Fuego / Agua",
        "Eléctrico / Psíquico",
        "Campeones"
    };

    private final JTextField nombreJugador1 = new JTextField();
    private final JTextField nombreJugador2 = new JTextField();
    private final JComboBox<String> mazoJugador1 = new JComboBox<>(MAZOS);
    private final JComboBox<String> mazoJugador2 = new JComboBox<>(MAZOS);

    public ConfiguracionPartida() {
        super("Pokémon TCG Pocket | Nueva partida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(construirContenido());
        setMinimumSize(new Dimension(820, 540));
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel construirContenido() {
        JPanel contenido = new JPanel(new BorderLayout(0, 24));
        contenido.setBackground(FONDO);
        contenido.setBorder(new EmptyBorder(34, 42, 30, 42));

        JPanel encabezado = new JPanel(new BorderLayout(0, 8));
        encabezado.setOpaque(false);
        JLabel titulo = new JLabel("Prepara la partida", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 30));
        titulo.setForeground(TINTA);
        JLabel subtitulo = new JLabel(
            "Cada jugador elige su nombre y uno de los mazos disponibles.",
            SwingConstants.CENTER
        );
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitulo.setForeground(SECUNDARIO);
        encabezado.add(titulo, BorderLayout.CENTER);
        encabezado.add(subtitulo, BorderLayout.SOUTH);
        contenido.add(encabezado, BorderLayout.NORTH);

        JPanel tarjetas = new JPanel(new GridLayout(1, 2, 22, 0));
        tarjetas.setOpaque(false);
        tarjetas.add(crearTarjeta("JUGADOR 1", nombreJugador1, mazoJugador1));
        tarjetas.add(crearTarjeta("JUGADOR 2", nombreJugador2, mazoJugador2));
        contenido.add(tarjetas, BorderLayout.CENTER);

        JButton comenzar = new JButton("Comenzar partida");
        comenzar.setFont(new Font("SansSerif", Font.BOLD, 16));
        comenzar.setForeground(Color.WHITE);
        comenzar.setBackground(ACENTO);
        comenzar.setFocusPainted(false);
        comenzar.setBorder(BorderFactory.createEmptyBorder(13, 34, 13, 34));
        comenzar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        comenzar.addActionListener(evento -> comenzarPartida());
        getRootPane().setDefaultButton(comenzar);

        JPanel pie = new JPanel();
        pie.setOpaque(false);
        pie.add(comenzar);
        contenido.add(pie, BorderLayout.SOUTH);
        return contenido;
    }

    private JPanel crearTarjeta(String titulo, JTextField campoNombre, JComboBox<String> selectorMazo) {
        JPanel tarjeta = new JPanel(new BorderLayout(0, 22));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            new EmptyBorder(24, 24, 24, 24)
        ));

        JLabel encabezado = new JLabel(titulo);
        encabezado.setFont(new Font("SansSerif", Font.BOLD, 13));
        encabezado.setForeground(ACENTO);
        tarjeta.add(encabezado, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridLayout(2, 1, 0, 16));
        campos.setOpaque(false);
        campos.add(crearCampo("Nombre del jugador", campoNombre));
        campos.add(crearSelector(selectorMazo));
        tarjeta.add(campos, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel crearCampo(String etiqueta, JTextField campo) {
        JPanel grupo = new JPanel(new BorderLayout(0, 8));
        grupo.setOpaque(false);
        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(TINTA);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        campo.setMargin(new Insets(10, 12, 10, 12));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225)),
            BorderFactory.createEmptyBorder(2, 3, 2, 3)
        ));
        grupo.add(label, BorderLayout.NORTH);
        grupo.add(campo, BorderLayout.CENTER);
        return grupo;
    }

    private JPanel crearSelector(JComboBox<String> selector) {
        JPanel grupo = new JPanel(new BorderLayout(0, 8));
        grupo.setOpaque(false);
        JLabel label = new JLabel("Mazo");
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(TINTA);
        selector.setFont(new Font("SansSerif", Font.PLAIN, 15));
        selector.setSelectedIndex(0);
        grupo.add(label, BorderLayout.NORTH);
        grupo.add(selector, BorderLayout.CENTER);
        return grupo;
    }

    private void comenzarPartida() {
        String nombre1 = nombreJugador1.getText().trim();
        String nombre2 = nombreJugador2.getText().trim();
        int opcionMazo1 = mazoJugador1.getSelectedIndex();
        int opcionMazo2 = mazoJugador2.getSelectedIndex();

        if (nombre1.isEmpty() || nombre2.isEmpty() || opcionMazo1 == 0 || opcionMazo2 == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Completa el nombre y selecciona un mazo para ambos jugadores.",
                "Faltan datos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        CatalogoCartas catalogo = new CatalogoCartas();
        Mazo mazo1 = catalogo.crearMazoPredefinido(opcionMazo1);
        Mazo mazo2 = catalogo.crearMazoPredefinido(opcionMazo2);
        if (mazo1 == null || mazo2 == null) {
            JOptionPane.showMessageDialog(
                this,
                "No se pudieron preparar los mazos seleccionados.",
                "Error al iniciar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Jugador jugador1 = new Jugador(nombre1, mazo1, new ZonaEnergia(mazo1.getTiposEnergia()));
        Jugador jugador2 = new Jugador(nombre2, mazo2, new ZonaEnergia(mazo2.getTiposEnergia()));
        MotorGrafico motor = new MotorGrafico(jugador1, jugador2);

        Tablero tablero = new Tablero(motor);
        dispose();
        tablero.setVisible(true);
    }
}
