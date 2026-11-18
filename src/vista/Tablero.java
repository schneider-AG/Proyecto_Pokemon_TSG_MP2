package vista;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import modelo.Carta;
import modelo.Jugador;
import modelo.MotorGrafico;
import modelo.MotorJuego;
import modelo.PanelFondo;
import modelo.Pokemon;
import modelo.PokemonBasico;
import modelo.TipoEnergia;
import modelo.ZonaEnergia;
import modelo.Jugador;
public class Tablero extends javax.swing.JFrame {

    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 140;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Tablero.class.getName());
    private MotorJuego motor;
    private ZonaEnergia zonaEnergiaJugadorActual;
    private MotorGrafico motorGrafico;
    private JPopupMenu popup;
    private JPanel panelSeleccionado;
    private Carta cartaSeleccionada1;
    private PokemonPanel panelActivoRival;
    private JPanel panelCentroTablero;
    private JScrollPane scrollMano;
    @SuppressWarnings("serial")
    private Image imagenDorsoMazo;
    private double escalaCartas = 1.0;

    MouseAdapter clicEnergia = new MouseAdapter() {
        //TipoEnergia energiaActual;
    @Override public void mouseClicked(MouseEvent e) {
        JLabel origen = (JLabel) e.getSource();
         // motor.getJugadorActual().getZonaEnergia().getEnergiaActual()

        if(esLabelDeEnergia(origen)){
            TipoEnergia actual = TipoEnergia.FUEGO;
        if (actual == null) {
            registrarEvento("Ya usaste la energía de este turno.");
            return;
        }
        if (origen != labelDe(actual)) {
            registrarEvento("Esa no es la energía de este turno.");
            return;
        }
        energiaSeleccionada = true;
        cartaSeleccionada1 = null;
        //energiaActual = actual;
        registrarEvento("Energía seleccionada. Elige un Pokémon.");
        }
        /*if(energiaSeleccionada){
            Component clicado = (Component) e.getSource();
            if (SwingUtilities.isDescendingFrom(clicado, panelActivo)) {
                Pokemon activo = motor.jugadorActual().getCampo().getPokemonActivo();

                if (activo != null) {
                    motor.jugadorActual().getCampo().getPokemonActivo().adjuntarEnergia(energiaActual); // Tu método para darle la energía
                    energiaSeleccionada = false;
                    registrarEvento("Energía asignada a " + activo.getNombre() + ".");
                } else {
                    registrarEvento("Este panel está vacío, no hay Pokémon activo.");
                }
            }
            Pokemon objetivo = motor.jugadorActual().getCampo().getPokemonActivo();
            if (objetivo != null) {
                // Llama aquí al método de tu motor que une la energía actual al Pokémon:
                motor.jugadorActual().getCampo().getPokemonActivo().adjuntarEnergia(energiaActual); // Asegúrate de que este método exista y funcione correctamente

                energiaSeleccionada = false; // Reinicia la selección
                registrarEvento("Energía asignada a " + objetivo.getNombre() + ".");
                usarEnergia(origen); // Oculta la energía usada
                 // Refresca los paneles para mostrar la energía en el Pokémon
            }
        }*/
    }
};
    
    public void setMotor(MotorJuego motor) {
        this.motor = motor;
    }

    /**
     * Creates new form Tablero
     */
    public Tablero(MotorGrafico motor) {
    this.motorGrafico = motor;
    initComponents();
    armarInterfaz();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(panelFondo, BorderLayout.CENTER);
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    motorGrafico.iniciarPartida();
    mostrarMano(motorGrafico.getJugador1().getMano()); // Reparte las manos iniciales y define el jugador 1
    conectarEventos();             // Activa los clics en el tablero y energías
    configurarEventos();           // Activa el teclado
    actualizar();                  // Dibuja la mano, energía y tablero por primera vez
    
    registrarEvento("¡Comienza la partida! Turno de: " + motorGrafico.jugadorActual().getNombre());
}

    private void armarInterfaz() {
        panelActivoRival = new PokemonPanel(null);
        panelActivoRival.setBackground(panelActivo.getBackground());
        ImageIcon dorso = (ImageIcon) jLabel3.getIcon();
        imagenDorsoMazo = dorso == null ? null : dorso.getImage();

        panelFondo.removeAll();
        panelFondo.setLayout(new BorderLayout());

        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setOpaque(true);
        contenido.setBackground(panelFondo.getBackground());
        contenido.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        panelFondo.add(contenido, BorderLayout.CENTER);

        JPanel izquierda = crearColumnaIzquierda();
        JPanel centro = crearColumnaCentral();
        JPanel derecha = crearColumnaDerecha();

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.insets = new Insets(0, 8, 0, 8);
        c.gridx = 0;
        c.weightx = 0;
        contenido.add(izquierda, c);
        c.gridx = 1;
        c.weightx = 1;
        contenido.add(centro, c);
        c.gridx = 2;
        c.weightx = 0;
        contenido.add(derecha, c);

        ajustarTamanosCartas();
        panelFondo.revalidate();
        panelFondo.repaint();
    }

    private JPanel crearColumnaIzquierda() {
        JPanel columna = new JPanel(new BorderLayout(0, 18));
        columna.setOpaque(false);
        columna.setPreferredSize(new Dimension(190, 400));

        JPanel superior = new JPanel();
        superior.setOpaque(false);
        superior.setLayout(new BoxLayout(superior, BoxLayout.Y_AXIS));
        jLabel17.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabel17.setHorizontalAlignment(JLabel.CENTER);
        jLabel18.setHorizontalAlignment(JLabel.CENTER);
        superior.add(jLabel17);
        superior.add(Box.createVerticalStrut(18));
        superior.add(crearBloqueMazo(jLabel2, jLabel3, jLabel10));
        columna.add(superior, BorderLayout.NORTH);

        JPanel inferior = new JPanel();
        inferior.setOpaque(false);
        inferior.setLayout(new BoxLayout(inferior, BoxLayout.Y_AXIS));
        jLabel19.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabel18.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel energias = new JPanel(new GridLayout(2, 2, 6, 6));
        energias.setOpaque(false);
        energias.add(jPanel15);
        energias.add(jPanel16);
        energias.add(jPanel18);
        energias.add(jPanel13);
        inferior.add(jLabel19);
        inferior.add(Box.createVerticalStrut(8));
        inferior.add(energias);
        inferior.add(Box.createVerticalStrut(12));
        inferior.add(jLabel18);
        columna.add(inferior, BorderLayout.SOUTH);
        return columna;
    }

    private JPanel crearColumnaCentral() {
        JPanel columna = new JPanel(new BorderLayout(0, 8));
        columna.setOpaque(false);

        jLabel20.setAlignmentX(Component.CENTER_ALIGNMENT);
        columna.add(jLabel20, BorderLayout.NORTH);

        panelCentroTablero = new JPanel();
        panelCentroTablero.setOpaque(false);
        panelCentroTablero.setLayout(new BoxLayout(panelCentroTablero, BoxLayout.Y_AXIS));

        JPanel bancaRival = crearFilaCartas(panelBanco5, panelBanco4);
        JPanel activoRival = crearPilaCarta(jLabel11, panelActivoRival);
        JPanel activoPropio = crearPilaCarta(jLabel12, panelActivo);
        JPanel bancaPropia = crearFilaCartas(panelBanco3, panelBanco1);
        jLabel20.setHorizontalAlignment(JLabel.CENTER);
        jLabel11.setHorizontalAlignment(JLabel.CENTER);
        jLabel12.setHorizontalAlignment(JLabel.CENTER);

        panelCentroTablero.add(Box.createVerticalGlue());
        panelCentroTablero.add(bancaRival);
        panelCentroTablero.add(Box.createVerticalGlue());
        panelCentroTablero.add(activoRival);
        panelCentroTablero.add(Box.createVerticalGlue());
        panelCentroTablero.add(activoPropio);
        panelCentroTablero.add(Box.createVerticalGlue());
        panelCentroTablero.add(bancaPropia);
        panelCentroTablero.add(Box.createVerticalGlue());

        panelCentroTablero.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarTamanosCartas();
            }
        });
        columna.add(panelCentroTablero, BorderLayout.CENTER);

        panelMano.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panelMano.setOpaque(false);
        scrollMano = new JScrollPane(panelMano,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollMano.setBorder(BorderFactory.createEmptyBorder());
        scrollMano.setOpaque(false);
        scrollMano.getViewport().setOpaque(false);

        JPanel pie = new JPanel();
        pie.setOpaque(false);
        pie.setLayout(new BoxLayout(pie, BoxLayout.Y_AXIS));
        scrollMano.setAlignmentX(Component.CENTER_ALIGNMENT);
        pie.add(scrollMano);
        JPanel inferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        inferior.setOpaque(false);
        inferior.add(jButton1);
        pie.add(inferior);
        columna.add(pie, BorderLayout.SOUTH);
        return columna;
    }

    private JPanel crearColumnaDerecha() {
        JPanel columna = new JPanel(new BorderLayout(0, 12));
        columna.setOpaque(false);
        columna.setPreferredSize(new Dimension(250, 400));

        jLabel7.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabel7.setHorizontalAlignment(JLabel.CENTER);
        columna.add(jLabel7, BorderLayout.NORTH);
        jScrollPane1.setPreferredSize(new Dimension(235, 340));
        columna.add(jScrollPane1, BorderLayout.CENTER);

        JPanel inferior = new JPanel();
        inferior.setOpaque(false);
        inferior.setLayout(new BoxLayout(inferior, BoxLayout.Y_AXIS));
        inferior.add(crearBloqueMazo(jLabel6, jLabel4, jLabel9));
        inferior.add(Box.createVerticalStrut(12));
        jLabel8.setAlignmentX(Component.CENTER_ALIGNMENT);
        inferior.add(jLabel8);
        columna.add(inferior, BorderLayout.SOUTH);
        return columna;
    }

    private JPanel crearBloqueMazo(JLabel titulo, JLabel dorso, JLabel contador) {
        JPanel bloque = new JPanel();
        bloque.setOpaque(false);
        bloque.setLayout(new BoxLayout(bloque, BoxLayout.Y_AXIS));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        dorso.setAlignmentX(Component.CENTER_ALIGNMENT);
        contador.setAlignmentX(Component.CENTER_ALIGNMENT);
        bloque.add(titulo);
        bloque.add(Box.createVerticalStrut(8));
        bloque.add(dorso);
        bloque.add(Box.createVerticalStrut(8));
        bloque.add(contador);
        return bloque;
    }

    private JPanel crearFilaCartas(PokemonPanel... cartas) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        fila.setOpaque(false);
        for (PokemonPanel carta : cartas) {
            fila.add(carta);
        }
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        return fila;
    }

    private JPanel crearPilaCarta(JLabel etiqueta, PokemonPanel carta) {
        JPanel pila = new JPanel();
        pila.setOpaque(false);
        pila.setLayout(new BoxLayout(pila, BoxLayout.Y_AXIS));
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        carta.setAlignmentX(Component.CENTER_ALIGNMENT);
        pila.add(etiqueta);
        pila.add(Box.createVerticalStrut(5));
        pila.add(carta);
        pila.setAlignmentX(Component.CENTER_ALIGNMENT);
        pila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        return pila;
    }

    private void ajustarTamanosCartas() {
        int ancho = panelCentroTablero == null ? 0 : panelCentroTablero.getWidth();
        int alto = panelCentroTablero == null ? 0 : panelCentroTablero.getHeight();
        if (ancho > 0 && alto > 0) {
            escalaCartas = Math.max(0.75, Math.min(1.25,
                Math.min(ancho / 950.0, alto / 820.0)));
        }

        int anchoCarta = (int) Math.round(CARD_WIDTH * escalaCartas);
        int altoCarta = (int) Math.round(CARD_HEIGHT * escalaCartas);
        establecerTamano(panelActivo, anchoCarta, altoCarta);
        establecerTamano(panelActivoRival, anchoCarta, altoCarta);
        establecerTamano(panelBanco1, anchoCarta, altoCarta);
        establecerTamano(panelBanco2, anchoCarta, altoCarta);
        establecerTamano(panelBanco3, anchoCarta, altoCarta);
        establecerTamano(panelBanco4, anchoCarta, altoCarta);
        establecerTamano(panelBanco5, anchoCarta, altoCarta);
        ajustarTamanoDorso(jLabel3, anchoCarta, altoCarta);
        ajustarTamanoDorso(jLabel4, anchoCarta, altoCarta);

        for (Component componente : panelMano.getComponents()) {
            if (componente instanceof PokemonPanel carta) {
                establecerTamano(carta, anchoCarta, altoCarta);
            }
        }
        int anchoMano = Math.max(400, panelMano.getComponentCount() * (anchoCarta + 30));
        panelMano.setPreferredSize(new Dimension(anchoMano, altoCarta + 10));
        panelMano.revalidate();
        if (panelCentroTablero != null) panelCentroTablero.revalidate();
    }

    private void establecerTamano(PokemonPanel panel, int ancho, int alto) {
        if (panel == null) return;
        Dimension tamano = new Dimension(ancho, alto);
        panel.setPreferredSize(tamano);
        panel.setMinimumSize(tamano);
        panel.setMaximumSize(tamano);
    }

    private void ajustarTamanoDorso(JLabel etiqueta, int ancho, int alto) {
        Dimension tamano = new Dimension(ancho, alto);
        etiqueta.setPreferredSize(tamano);
        etiqueta.setMinimumSize(tamano);
        etiqueta.setMaximumSize(tamano);
        if (imagenDorsoMazo != null) {
            Image imagen = imagenDorsoMazo.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            etiqueta.setIcon(new ImageIcon(imagen));
        }
    }

    private void initComponents() {

        panelFondo = new PanelFondo("/Media/Background2.png");
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelBanco1 = new PokemonPanel(null);
        jProgressBar7 = new javax.swing.JProgressBar();
        panelBanco2 = new PokemonPanel(null);
        jProgressBar9 = new javax.swing.JProgressBar();
        panelBanco3 = new PokemonPanel(null);
        jProgressBar6 = new javax.swing.JProgressBar();
        panelBanco4 = new PokemonPanel(null);
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel11 = new javax.swing.JPanel();
        panelActivo = new PokemonPanel(null);
        jProgressBar8 = new javax.swing.JProgressBar();
        panelBanco5 = new PokemonPanel(null);
        jProgressBar5 = new javax.swing.JProgressBar();
        jLabel5 = new javax.swing.JLabel();
        panelMano = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        lblEnergiaAgua = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        lblEnergiaFuego = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        lblEnergiaElectrica = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        lblEnergiaPsiquica = new javax.swing.JLabel();
        PanelPopup panelInformacion = new PanelPopup();
        popup = new JPopupMenu();
        popup.add(panelInformacion.getPanel());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelFondo.setBackground(new java.awt.Color(255, 255, 255));
        panelFondo.setPreferredSize(new java.awt.Dimension(550, 512));
        panelFondo.setLayout(null);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/CartaImagen.png"))); // NOI18N
        panelFondo.add(jLabel3);
        jLabel3.setBounds(100, 220, 50, 70);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/CartaImagen.png"))); // NOI18N
        panelFondo.add(jLabel4);
        jLabel4.setBounds(360, 450, 50, 70);

        panelBanco1.setBackground(new java.awt.Color(204, 255, 255));



        panelFondo.add(panelBanco1);
        panelBanco1.setBounds(289, 525, 52, 70);

        panelBanco2.setBackground(new java.awt.Color(204, 255, 255));

        panelFondo.add(panelBanco2);
        panelBanco2.setBounds(249, 210, 52, 70);

        panelBanco3.setBackground(new java.awt.Color(204, 255, 255));



        panelFondo.add(panelBanco3);
        panelBanco3.setBounds(209, 525, 52, 70);

        panelBanco4.setBackground(new java.awt.Color(204, 255, 255));




        panelFondo.add(panelBanco4);
        panelBanco4.setBounds(289, 130, 52, 70);

        jPanel11.setBackground(new java.awt.Color(204, 204, 255));
        jPanel11.setLayout(null);
        panelFondo.add(jPanel11); 
        jPanel11.setBounds(0, 40, 550, 80);

        panelActivo.setBackground(new java.awt.Color(204, 255, 255));


        panelFondo.add(panelActivo);
        panelActivo.setBounds(249, 445, 52, 70);

        panelBanco5.setBackground(new java.awt.Color(204, 255, 255));



        panelFondo.add(panelBanco5);
        panelBanco5.setBounds(209, 130, 52, 70);
        panelFondo.add(jLabel5);
        jLabel5.setBounds(420, 10, 0, 0);

        panelMano.setBackground(new java.awt.Color(204, 204, 255));
        panelMano.setLayout(null);
        panelFondo.add(panelMano);
        panelMano.setBounds(0, 610, 550, 70);

        jPanel16.setBackground(new java.awt.Color(204, 255, 204));
        jPanel16.setLayout(null);

        lblEnergiaAgua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/EnergiaAgua.png"))); // NOI18N
        jPanel16.add(lblEnergiaAgua);
        lblEnergiaAgua.setBounds(0, 0, 50, 50);

        panelFondo.add(jPanel16);
        jPanel16.setBounds(30, 340, 50, 50);

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(204, 0, 51));
        jLabel2.setText("Mazo rival");
        jLabel2.setOpaque(true);
        panelFondo.add(jLabel2);
        jLabel2.setBounds(245, 20, 60, 16);

        jPanel15.setBackground(new java.awt.Color(204, 255, 204));
        jPanel15.setLayout(null);

        lblEnergiaFuego.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/EnergiaFuego.png"))); // NOI18N
        jPanel15.add(lblEnergiaFuego);
        lblEnergiaFuego.setBounds(0, 0, 50, 50);

        panelFondo.add(jPanel15);
        jPanel15.setBounds(30, 290, 50, 50);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        panelFondo.add(jScrollPane1);
        jScrollPane1.setBounds(360, 170, 110, 220);

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(204, 0, 51));
        jLabel6.setText("Mazo propio");
        jLabel6.setOpaque(true);
        panelFondo.add(jLabel6);
        jLabel6.setBounds(240, 592, 70, 16);

        jLabel7.setText("Premios: 1/3");
        jLabel7.setOpaque(true);
        panelFondo.add(jLabel7);
        jLabel7.setBounds(390, 20, 66, 16);

        jButton1.setText("Pasar turno");
        panelFondo.add(jButton1);
        jButton1.setBounds(225, 684, 100, 24);

        jLabel8.setText("Premios: 1/3");
        jLabel8.setOpaque(true);
        panelFondo.add(jLabel8);
        jLabel8.setBounds(390, 690, 72, 16);

        jLabel9.setText("Número de cartas : 0");
        jLabel9.setOpaque(true);
        panelFondo.add(jLabel9);
        jLabel9.setBounds(330, 420, 110, 20);

        jLabel10.setText("Número de cartas : 0");
        jLabel10.setOpaque(true);
        panelFondo.add(jLabel10);
        jLabel10.setBounds(69, 190, 110, 20);

        jLabel11.setText("Activo Rival");
        jLabel11.setOpaque(true);
        panelFondo.add(jLabel11);
        jLabel11.setBounds(240, 290, 70, 16);

        jLabel12.setText("Activo propio");
        jLabel12.setOpaque(true);
        panelFondo.add(jLabel12);
        jLabel12.setBounds(240, 425, 73, 16);

        jPanel18.setBackground(new java.awt.Color(204, 255, 204));
        jPanel18.setLayout(null);

        lblEnergiaElectrica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/EnergiaElectrica.png"))); // NOI18N
        jPanel18.add(lblEnergiaElectrica);
        lblEnergiaElectrica.setBounds(0, 0, 50, 50);

        panelFondo.add(jPanel18);
        jPanel18.setBounds(30, 390, 50, 50);

        jLabel17.setText("Rival: " + motorGrafico.getJugador1().getNombre());
        jLabel17.setOpaque(true);
        panelFondo.add(jLabel17);
        jLabel17.setBounds(20, 20, 120, 30);

        jLabel18.setText("Mi nombre: " + motorGrafico.getJugador2().getNombre());
        jLabel18.setOpaque(true);
        panelFondo.add(jLabel18);
        jLabel18.setBounds(20, 690, 90, 30);

        jLabel19.setText("Estado de energía");
        jLabel19.setOpaque(true);
        panelFondo.add(jLabel19);
        jLabel19.setBounds(20, 500, 100, 16);

        jLabel20.setText("Turno " + motorGrafico.getTurno() + " - " + motorGrafico.jugadorActual().getNombre());
        jLabel20.setOpaque(true);
        panelFondo.add(jLabel20);
        jLabel20.setBounds(223, 0, 104, 16);

        jPanel13.setBackground(new java.awt.Color(204, 255, 204));
        jPanel13.setLayout(null);

        lblEnergiaPsiquica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Media/EnergiaPsiquica.png"))); // NOI18N
        lblEnergiaPsiquica.setToolTipText("");
        jPanel13.add(lblEnergiaPsiquica);
        lblEnergiaPsiquica.setBounds(0, 0, 50, 50);

        panelFondo.add(jPanel13);
        jPanel13.setBounds(30, 440, 50, 50);

        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel18.setVisible(false);
        jPanel13.setVisible(false);

        /*lblEnergiaFuego.addMouseListener(clicEnergia);
        lblEnergiaAgua.addMouseListener(clicEnergia);
        lblEnergiaElectrica.addMouseListener(clicEnergia);
        lblEnergiaPsiquica.addMouseListener(clicEnergia);*/


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(panelFondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1288, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelFondo, javax.swing.GroupLayout.PREFERRED_SIZE, 716, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );

        pack();
    }// </editor-fold>       
    private void mostrarMano(java.util.List<modelo.Carta> mano) {
    panelMano.removeAll();                       // limpia la mano anterior
    panelMano.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 4, 0));
    panelMano.setOpaque(false);

    for (Carta carta : mano) {
        PokemonPanel cp = new PokemonPanel(carta);
        cp.getCarta().getNombre();   // reemplaza todo el bloque del JLabel
        cp.addMouseListener(clickTablero);
        panelMano.add(cp);
    }

    panelMano.revalidate();   // recalcula el layout
    panelMano.repaint();      // redibuja
    ajustarTamanosCartas();
}
    private void actualizarMano() {
        Jugador jugador = motorGrafico.jugadorActual();   // la misma fuente que usa la validación

        panelMano.removeAll();                            // 1. borra lo anterior
        for (Carta carta : jugador.getMano()) {           // 2. una por cada carta del modelo
            PokemonPanel cp = new PokemonPanel(carta);
            cp.addMouseListener(clickTablero);
            panelMano.add(cp);
        }
        panelMano.revalidate();                           // 3. recalcula el layout
        panelMano.repaint();                              // 4. redibuja
        ajustarTamanosCartas();
    }

private void probarMano(Jugador jugador) {

    panelMano.removeAll();
    panelMano.setLayout(new java.awt.FlowLayout(FlowLayout.CENTER, 4, 0));
    panelMano.setOpaque(false);

    for (Carta n : jugador.getMano()) {
        PokemonPanel panel = new PokemonPanel(n);
        panel.mostrar(n);
        panelMano.add(panel);
    }
    panelMano.revalidate();
    panelMano.repaint();
}


    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel lblEnergiaFuego; //Energia fuego
    private javax.swing.JLabel lblEnergiaAgua; //Energia agua
    private javax.swing.JLabel lblEnergiaElectrica; //Energia electrica
    private javax.swing.JLabel lblEnergiaPsiquica; //Energia psiquica
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel panelFondo;
    private javax.swing.JPanel panelMano; // Panel mano
    private javax.swing.JPanel jPanel11; // Panel mano rival
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private PokemonPanel panelBanco1;
    private PokemonPanel panelBanco2;
    private PokemonPanel panelBanco3;
    private PokemonPanel panelBanco5;
    private PokemonPanel panelBanco4;
    private PokemonPanel panelActivo; // Panel pokemon activo propio
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar5;
    private javax.swing.JProgressBar jProgressBar6;
    private javax.swing.JProgressBar jProgressBar7;
    private javax.swing.JProgressBar jProgressBar8;
    private javax.swing.JProgressBar jProgressBar9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration        
    
    /// Conectar eventos
    private Carta cartaSeleccionada = null;
    private boolean energiaSeleccionada = false;


    private void actualizarEnergia() {
        TipoEnergia actual = TipoEnergia.FUEGO; // motor.getJugadorActual().getZonaEnergia().getEnergiaActual();
        resaltar(lblEnergiaFuego,     actual == TipoEnergia.FUEGO);
        resaltar(lblEnergiaAgua,      actual == TipoEnergia.AGUA);
        resaltar(lblEnergiaElectrica, actual == TipoEnergia.ELECTRICO);
        resaltar(lblEnergiaPsiquica,  actual == TipoEnergia.PSIQUICO);
    }

    private void resaltar(JLabel lbl, boolean disponible) {
        JPanel panelAux = (JPanel) lbl.getParent();
        panelAux.setVisible(disponible);
    }

    private void usarEnergia(JLabel lbl) {
        JPanel panelAux = (JPanel) lbl.getParent();
        panelAux.setVisible(false);
    }

    private JLabel labelDe(TipoEnergia t) {
        switch (t) {
            case FUEGO:     return lblEnergiaFuego;
            case AGUA:      return lblEnergiaAgua;
            case ELECTRICO: return lblEnergiaElectrica;
            default:        return lblEnergiaPsiquica;
        }
    }

    
    public void registrarEvento(String mensaje) {
        jTextArea1.append(mensaje + "\n");
    }

    private boolean esLabelDeEnergia(Object origen) {
    return origen == lblEnergiaFuego || origen == lblEnergiaAgua || origen == lblEnergiaElectrica || origen == lblEnergiaPsiquica;
    }
private void configurarEventos() {
    this.setFocusable(true);

    // Presionar cualquier tecla
    this.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (pantalla.width - popup.getPreferredSize().width) / 2;
        int y = (pantalla.height - popup.getPreferredSize().height) / 2;
        popup.show(panelFondo, x, y);
        }
    });
}


private final MouseAdapter clickTablero = new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        Object origen = e.getSource();
        if (!(origen instanceof PokemonPanel)) return;

        PokemonPanel panelClickeado = (PokemonPanel) origen;
        Jugador jugador = motorGrafico.jugadorActual();

        // PRIMER CLIC: El jugador tocó una carta que está dentro de panelMano
        if (panelClickeado.getParent() == panelMano) {
            Carta carta = panelClickeado.getCarta();
            if (carta == null) return;

            String error = jugador.validarColocacion(carta);
            if (error != null) {
                JOptionPane.showMessageDialog(Tablero.this, error);
                return;
            }

            cartaSeleccionada = carta;
            registrarEvento("Seleccionaste: " + carta.getNombre()); // Feedback visual
            return;
        }

        // SEGUNDO CLIC: El jugador tocó panelActivo o algún panelBanco
        if (origen == panelActivo && jugador.getCampo().getPokemonActivo() != null) {
        int x = (panelFondo.getWidth() - popup.getPreferredSize().width) / 2;
        int y = (panelFondo.getHeight() - popup.getPreferredSize().height) / 2;
        popup.show(panelFondo, x, y);
        } else if (origen == panelActivo || esPanelBanco(origen)) {
            if (cartaSeleccionada == null) {
                JOptionPane.showMessageDialog(Tablero.this, "Primero haz clic en una carta de tu mano");
                return;
            }

            // Verifica que el espacio del tablero donde hizo clic esté vacío
            if (panelClickeado.getCarta() != null) {
                JOptionPane.showMessageDialog(Tablero.this, "Ese espacio ya está ocupado");
                return;
            }

            // Validación específica si tocó el espacio activo
            if (origen == panelActivo && jugador.getCampo().getPokemonActivo() != null) {
                JOptionPane.showMessageDialog(Tablero.this, "Ya tienes un Pokémon activo");
                return;
            }

            // Coloca la carta en el modelo, limpia la selección y refresca la pantalla
            jugador.colocarPokemonEnJuego(cartaSeleccionada);
            cartaSeleccionada = null;
            actualizar();
    } else {
        JOptionPane.showMessageDialog(Tablero.this, "Primero selecciona un Pokémon de tu mano");
    }
        actualizarMano();
}

};

private boolean esPanelBanco(Object origen) {
    return origen == panelBanco1 || origen == panelBanco3;
}

private void conectarEventos() {
    panelActivo.addMouseListener(clickTablero);
    panelBanco1.addMouseListener(clickTablero);
    panelBanco3.addMouseListener(clickTablero);
}

    private void actualizar() {
        Jugador propio = motorGrafico.jugadorActual();
        Jugador rival = propio == motorGrafico.getJugador1()
            ? motorGrafico.getJugador2() : motorGrafico.getJugador1();

        panelActivo.mostrar(propio.getCampo().getPokemonActivo());
        panelActivoRival.mostrar(rival.getCampo().getPokemonActivo());
        mostrarBanca(propio.getCampo().getBanco(), panelBanco3, panelBanco1);
        mostrarBanca(rival.getCampo().getBanco(), panelBanco5, panelBanco4);

        jLabel17.setText("Rival: " + rival.getNombre());
        jLabel18.setText("Mi nombre: " + propio.getNombre());
        jLabel20.setText("Turno " + motorGrafico.getTurno() + " - " + propio.getNombre());
        jLabel7.setText("Premios: " + rival.getPuntosPremio() + "/3");
        jLabel8.setText("Premios: " + propio.getPuntosPremio() + "/3");
        jLabel10.setText("Número de cartas: " + rival.getMazo().getCantidadCartas());
        jLabel9.setText("Número de cartas: " + propio.getMazo().getCantidadCartas());
        ajustarTamanosCartas();
    }

    private void mostrarBanca(java.util.List<Pokemon> banco, PokemonPanel... espacios) {
        for (int i = 0; i < espacios.length; i++) {
            espacios[i].mostrar(i < banco.size() ? banco.get(i) : null);
        }
    }

    /*private void prepararPrueba() {
    // 1. Crear un Pokémon de prueba (ajusta los parámetros a tu constructor de Pokemon)
    Pokemon pokemonPrueba = new PokemonBasico("Charmander", TipoEnergia.FUEGO, (short)60, (short)60, (byte) 1);

    // 2. Asignarlo manualmente como Pokémon activo del jugador actual en el motor
    Jugador jugador = motor.jugadorActual();
    jugador.colocarPokemonEnJuego(pokemonPrueba); // Asegúrate de que este método exista y funcione correctamente

    // (Opcional) Si tu zona de energía empieza vacía, fuerza una energía para probar:
    // jugador.getZonaEnergia().generarEnergia(); 
    JLabel lblNombrePokemon = new JLabel();
    panelActivo.add(lblNombrePokemon);
    // 3. Mostrarlo visualmente en el JLabel que está dentro de tu panel de Pokémon
    lblNombrePokemon.setText(pokemonPrueba.getNombre() + " (PS: " + pokemonPrueba.getPsActual() + ")");
    lblNombrePokemon.setVisible(true);
    panelActivo.setVisible(true);
    }*/
};
