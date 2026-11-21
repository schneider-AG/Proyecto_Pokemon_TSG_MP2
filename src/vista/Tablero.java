package vista;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.PopupMenu;
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
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import modelo.Carta;
import modelo.Entrenador;
import modelo.Jugador;
import modelo.MotorGrafico;
import modelo.PanelFondo;
import modelo.Pokemon;
import modelo.PokemonBasico;
import modelo.TipoEnergia;
import modelo.ZonaEnergia;
import modelo.Jugador;
public class Tablero extends javax.swing.JFrame {

    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 135;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Tablero.class.getName());
    private MotorGrafico motor;
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
    private boolean energiaSeleccionada = false;
    private Carta cartaSeleccionada = null;
    private boolean esperandoReemplazoBanca = false;
    private boolean esperandoRetiradaBanca = false;
    private Entrenador entrenadorPendiente = null;

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
    conectarEventos();             // Activa los clics en el tablero y energías
    actualizar();                  // Dibuja la mano, energía y tablero por primera vez
    actualizarMano();
    
    registrarEvento("Preparación inicial: " + motorGrafico.jugadorActual().getNombre()
        + ", elige tu Pokémon activo y coloca hasta dos Pokémon en la banca si quieres."
        + " Después pulsa Pasar turno para que el otro jugador prepare su campo.");
}

    private void armarInterfaz() {
        panelActivoRival = new PokemonPanel(null, false);
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

        JPanel filaJugador = new JPanel(new BorderLayout(8, 0));
        filaJugador.setOpaque(false);
        filaJugador.add(jLabel18, BorderLayout.CENTER);
        filaJugador.add(btnRendirse, BorderLayout.EAST);

        inferior.add(filaJugador);
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

        manoRival.setOpaque(false);
        manoRival.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 2));
        manoRival.setAlignmentX(Component.CENTER_ALIGNMENT);
        

        JPanel bancaRival = crearFilaCartas(panelBanco5, panelBanco4);
        JPanel activoRival = crearPilaCarta(jLabel11, panelActivoRival);
        JPanel activoPropio = crearPilaCarta(jLabel12, panelActivo);
        JPanel bancaPropia = crearFilaCartas(panelBanco3, panelBanco1);
        jLabel20.setHorizontalAlignment(JLabel.CENTER);
        jLabel11.setHorizontalAlignment(JLabel.CENTER);
        jLabel12.setHorizontalAlignment(JLabel.CENTER);

        panelCentroTablero.add(manoRival);
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
        actualizarManoRival();
        if (panelCentroTablero != null){
            panelCentroTablero.revalidate();
        }
    }

    private void establecerTamano(PokemonPanel panel, int ancho, int alto) {
        if (panel == null) return;
        Dimension tamano = new Dimension(ancho, alto);
        panel.setPreferredSize(tamano);
        panel.setMinimumSize(tamano);
        panel.setMaximumSize(tamano);
        panel.revalidate();
        panel.repaint();
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

    public void rendirse(){
    Jugador actual = motorGrafico.jugadorActual();
    int opcion = JOptionPane.showConfirmDialog(
            this,
            actual.getNombre() + ", ¿seguro que deseas rendirte?",
            "Confirmar rendición",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
    );
    if (opcion == JOptionPane.YES_OPTION) {
        Jugador ganador = motorGrafico.rendirse();
        registrarEvento(actual.getNombre() + " se rindió. ¡Gana " + ganador.getNombre() + "!");
        mostrarFinPartida();
    }
}

    private void mostrarFinPartida() {
        int opcion = JOptionPane.showOptionDialog(
            this,
            motorGrafico.anunciarGanador(),
            "Fin de la partida",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new Object[] {"Jugar otra partida", "Salir"},
            "Jugar otra partida"
        );

        if (opcion == 0) {
            new PantallaInicio().setVisible(true);
        }
        dispose();
    }

    private void initComponents() {

        panelFondo = new PanelFondo("/Media/Background2.png");
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelBanco1 = new PokemonPanel(null, false);
        jProgressBar7 = new javax.swing.JProgressBar();
        panelBanco2 = new PokemonPanel(null, false);
        jProgressBar9 = new javax.swing.JProgressBar();
        panelBanco3 = new PokemonPanel(null, false);
        jProgressBar6 = new javax.swing.JProgressBar();
        panelBanco4 = new PokemonPanel(null, false);
        jProgressBar1 = new javax.swing.JProgressBar();
        manoRival = new javax.swing.JPanel();
        panelActivo = new PokemonPanel(null, false);
        jProgressBar8 = new javax.swing.JProgressBar();
        panelBanco5 = new PokemonPanel(null, false);
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
        btnRendirse = new JButton();
        lblEnergiaPsiquica = new javax.swing.JLabel();
        panelInformacion = new PanelPopup();
        popup = new JPopupMenu();
        popup.add(panelInformacion.getPanel());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelFondo.setBackground(new java.awt.Color(255, 255, 255));
        panelFondo.setPreferredSize(new java.awt.Dimension(550, 512));
        panelFondo.setLayout(null);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cartas_POCKETCG/CartaImagen.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Cartas_POCKETCG/CartaImagen.png"))); // NOI18N

        panelBanco1.setBackground(new java.awt.Color(204, 255, 255));


        panelBanco2.setBackground(new java.awt.Color(204, 255, 255));


        panelBanco3.setBackground(new java.awt.Color(204, 255, 255));


        panelBanco4.setBackground(new java.awt.Color(204, 255, 255));


        manoRival.setBackground(new java.awt.Color(204, 204, 255));
        manoRival.setLayout(null);

        panelActivo.setBackground(new java.awt.Color(204, 255, 255));


        panelBanco5.setBackground(new java.awt.Color(204, 255, 255));



        panelMano.setBackground(new java.awt.Color(204, 204, 255));
        panelMano.setLayout(null);


        jPanel16.setBackground(new java.awt.Color(204, 255, 204));
        jPanel16.setLayout(null);



        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(204, 0, 51));
        jLabel2.setText("Mazo rival");
        jLabel2.setOpaque(true);


        jPanel15.setBackground(new java.awt.Color(204, 255, 204));
        jPanel15.setLayout(null);


        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);


        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(204, 0, 51));
        jLabel6.setText("Mazo propio");
        jLabel6.setOpaque(true);
        panelFondo.add(jLabel6);
        jLabel6.setBounds(240, 592, 70, 16);

        jLabel7.setText("Premios: 1/3");
        jLabel7.setOpaque(true);


        jButton1.setText("Pasar turno");


        jLabel8.setText("Premios: 1/3");
        jLabel8.setOpaque(true);


        jLabel9.setText("Número de cartas : 0");
        jLabel9.setOpaque(true);

        jLabel10.setText("Número de cartas : 0");
        jLabel10.setOpaque(true);


        jLabel11.setText("Activo Rival");
        jLabel11.setOpaque(true);

        jLabel12.setText("Activo propio");
        jLabel12.setOpaque(true);


        jPanel18.setBackground(new java.awt.Color(204, 255, 204));
        jPanel18.setLayout(null);



        jLabel17.setText("Rival: " + motorGrafico.getJugador1().getNombre());
        jLabel17.setOpaque(true);


        jLabel18.setText("Mi nombre: " + motorGrafico.getJugador2().getNombre());
        jLabel18.setOpaque(true);


        jLabel19.setText("Estado de energía");
        jLabel19.setOpaque(true);
        jLabel19.setToolTipText("Haz clic aquí para seleccionar la energía y luego en uno de tus Pokémon.");
        jLabel19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));


        jLabel20.setText("Turno " + motorGrafico.getTurno() + " - " + motorGrafico.jugadorActual().getNombre());
        jLabel20.setOpaque(true);


        jPanel13.setBackground(new java.awt.Color(204, 255, 204));
        jPanel13.setLayout(null);

        btnRendirse.setText("Rendirse");
        btnRendirse.setForeground(new Color(180, 30, 30));
        btnRendirse.setFont(btnRendirse.getFont().deriveFont(Font.BOLD, 11f));
        btnRendirse.setMargin(new Insets(2, 8, 2, 8));
        btnRendirse.setFocusable(false);

        jPanel15.setVisible(false);
        jPanel16.setVisible(false);
        jPanel18.setVisible(false);
        jPanel13.setVisible(false);


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

    private void actualizarMano() {
        Jugador jugador = motorGrafico.jugadorActual();   // la misma fuente que usa la validación

        panelMano.removeAll();                            // 1. borra lo anterior
        for (Carta carta : jugador.getMano()) {           // 2. una por cada carta del modelo
            PokemonPanel cp = new PokemonPanel(carta, true);
            cp.addMouseListener(clickTablero);
            panelMano.add(cp);
        }
        panelMano.revalidate();                           // 3. recalcula el layout
        panelMano.repaint();                              // 4. redibuja
        ajustarTamanosCartas();
    }

    private void ejecutarAtaque(int indice) {
    popup.setVisible(false);

    String error = motorGrafico.atacar(indice);
    if ("FIN_PARTIDA".equals(error)) {
        actualizar();
        mostrarFinPartida();
        return;
    }
    if (error != null) {
        JOptionPane.showMessageDialog(this, error);
        return;
    }

    registrarEvento(motorGrafico.jugadorActual().getNombre() + " atacó.");
    actualizar();

    if (motorGrafico.jugadorRival().getCampo().getPokemonActivo() == null) {
        esperandoReemplazoBanca = true;
    }
    actualizarMano();
    actualizarManoRival();
    pasarTurno();
}

    private void iniciarRetirada() {
        popup.setVisible(false);

    String error = motorGrafico.validarRetirada();
    if (error != null) {
        JOptionPane.showMessageDialog(this, error);
        return;
    }

    esperandoRetiradaBanca = true;
    panelBanco1.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
    panelBanco3.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));

    registrarEvento("Selecciona un Pokémon de tu banca para completar la retirada.");
    JOptionPane.showMessageDialog(this, "Haz clic sobre el Pokémon de tu banca que pasará al puesto Activo.");
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
    private javax.swing.JPanel manoRival; // Panel mano rival
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
    private PanelPopup panelInformacion;
    private JButton btnRendirse;
    // End of variables declaration        

    
    public void registrarEvento(String mensaje) {
        jTextArea1.append(mensaje + "\n");
    }
    
    private void pasarTurno(){
        Jugador actual = motorGrafico.jugadorActual();
        esperandoRetiradaBanca = false;
        if (actual.getCampo().getPokemonActivo() == null) {
            JOptionPane.showMessageDialog(this, "Debes colocar un Pokémon en el puesto Activo antes de pasar el turno.");
            return;
        }

        // 2. Limpiar cualquier carta o energía que haya quedado seleccionada a medias
        cartaSeleccionada = null;
        // energiaSeleccionada = false; // Descomenta si ya tienes esta variable

        // 3. Ejecutar el cambio en el motor
        String resultadoTurno = motorGrafico.pasarTurno();
        if ("FIN_PARTIDA".equals(resultadoTurno)) {
            mostrarFinPartida();
            return;
        }
        if (resultadoTurno != null) {
            JOptionPane.showMessageDialog(this, resultadoTurno);
            return;
        }



        actualizar();
        actualizarMano();
        actualizarManoRival();
        limpiarBordesMano();

        PantallaCambio pantalla = new PantallaCambio(
            null,
            motorGrafico.jugadorActual().getNombre(),
            "/Cartas_POCKETCG/PantallaCambioTurno.jpeg" // Ajusta a la ruta real de tu imagen
        );
        pantalla.setVisible(true);

        if (motorGrafico.estaEnPreparacionInicial()) {
            registrarEvento("Preparación inicial: " + motorGrafico.jugadorActual().getNombre()
                + ", elige tu Pokémon activo y coloca hasta dos Pokémon en la banca si quieres.");
        } else {
            registrarEvento("Turno " + motorGrafico.getTurno() + " - Juega: "
                + motorGrafico.jugadorActual().getNombre());
        }

        if (esperandoReemplazoBanca) {
        panelBanco1.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
        panelBanco3.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
        registrarEvento(motorGrafico.jugadorActual().getNombre() + ", tu Pokémon activo fue derrotado. Haz clic en uno de tu banca.");

        JOptionPane.showMessageDialog(
            this,
            motorGrafico.jugadorActual().getNombre() + ", tu Pokémon activo fue derrotado.\nHaz clic sobre el Pokémon de tu banca que pasará al puesto Activo.");

    }
}

    private final MouseAdapter clicEnergia = new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e) {
        if (esperandoReemplazoBanca) {
            registrarEvento("Primero debes elegir un Pokémon de tu banca para el puesto Activo.");
            return;
        }
        if (motorGrafico.jugadorActual().getEnergiaDisponible() == null) {
            registrarEvento("No tienes energía disponible este turno.");
            return;
        }

        energiaSeleccionada = !energiaSeleccionada;
        cartaSeleccionada = null;
        actualizar(); // Muestra u oculta el borde amarillo en jLabel19

        if (energiaSeleccionada) {
            registrarEvento("Energía seleccionada. Elige un Pokémon de tu campo.");
        }
    }
};
private void limpiarBordesMano() {
    for (Component comp : panelMano.getComponents()) {
        if (comp instanceof PokemonPanel pp) {
            pp.setBorder(null);
        }
    }
}

private final MouseAdapter clickTablero = new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e) {
        Component origen = (Component) e.getSource();

        while (origen != null && !(origen instanceof PokemonPanel)) {
            origen = origen.getParent();
        }

        PokemonPanel panelClickeado = null;
        if (origen instanceof PokemonPanel pp) {
            panelClickeado = pp;
        }

        Carta cartaClickeada = null;
        Pokemon objetivo = null;

        if (panelClickeado != null) {
            cartaClickeada = panelClickeado.getCarta();
            if (cartaClickeada instanceof Pokemon p) {
                objetivo = p;
            }
        }

        // 1. Reemplazo obligatorio tras KO
        if (esperandoReemplazoBanca) {
            entrenadorPendiente = null;
            if ((panelClickeado == panelBanco1 || panelClickeado == panelBanco3) && objetivo != null) {
                if (motorGrafico.jugadorActual().promoverDeBanca(objetivo)) {
                    esperandoReemplazoBanca = false;
                    cartaSeleccionada = null;
                    limpiarBordesMano();

                    panelBanco1.setBorder(null);
                    panelBanco3.setBorder(null);

                    registrarEvento(objetivo.getNombre() + " sube de la banca al puesto Activo.");
                    actualizar();
                    actualizarMano();
                }
            } else {
                JOptionPane.showMessageDialog(
                    Tablero.this,
                    "Debes hacer clic sobre un Pokémon de tu banca (no de tu mano ni del rival)."
                );
            }
            return;
        }

        // 2. Selección de banca para retirada
        if (esperandoRetiradaBanca) {
            entrenadorPendiente = null;
            if ((panelClickeado == panelBanco1 || panelClickeado == panelBanco3) && objetivo != null) {
                String error = motorGrafico.retirar(objetivo);
                esperandoRetiradaBanca = false;
                panelBanco1.setBorder(null);
                panelBanco3.setBorder(null);

                if (error != null) {
                    JOptionPane.showMessageDialog(Tablero.this, error);
                } else {
                    registrarEvento(objetivo.getNombre() + " entra al puesto Activo por retirada.");
                    actualizar();
                }
            } else {
                esperandoRetiradaBanca = false;
                panelBanco1.setBorder(null);
                panelBanco3.setBorder(null);
                registrarEvento("Retirada cancelada.");
            }
            return;
        }

        // 3. Si hay un ENTRENADOR seleccionado (pendiente de 2.º clic)
        if (entrenadorPendiente != null) {
            if (!entrenadorPendiente.requiereObjetivoTablero()) {
                // Sin objetivo (Investigación del Profesor, Poké Ball, Turbo Velocidad):
                // Solo se activa si hiciste clic en el FONDO (panelClickeado == null)
                if (panelClickeado == null) {
                    String error = motorGrafico.jugarEntrenador(entrenadorPendiente, null);
                    if (error != null) {
                        JOptionPane.showMessageDialog(Tablero.this, error);
                    } else {
                        registrarEvento(motorGrafico.jugadorActual().getNombre() + " jugó " + entrenadorPendiente.getNombre() + ".");
                        actualizar();
                        actualizarMano();
                    }
                } else {
                    // Si tocaste la misma carta, otra carta o cualquier PokemonPanel, se deselecciona
                    registrarEvento("Uso de " + entrenadorPendiente.getNombre() + " cancelado.");
                }
            } else {
                // Con objetivo (Misty, Poción):
                // Solo se activa si tocaste uno de tus Pokémon en juego (Activo o Banca)
                boolean esObjetivoValido = (panelClickeado == panelActivo
                        || panelClickeado == panelBanco1
                        || panelClickeado == panelBanco3) && objetivo != null;

                if (esObjetivoValido) {
                    String error = motorGrafico.jugarEntrenador(entrenadorPendiente, objetivo);
                    if (error != null) {
                        JOptionPane.showMessageDialog(Tablero.this, error);
                    } else {
                        registrarEvento(motorGrafico.jugadorActual().getNombre() + " usó " + entrenadorPendiente.getNombre() + " en " + objetivo.getNombre() + ".");
                        actualizar();
                        actualizarMano();
                    }
                } else {
                    // Si tocaste el fondo, la misma carta, un hueco vacío o al rival, se deselecciona
                    registrarEvento("Uso de " + entrenadorPendiente.getNombre() + " cancelado.");
                }
            }

            entrenadorPendiente = null;
            limpiarBordesMano();
            return;
        }

        // 4. PRIMER CLIC: El jugador tocó una carta en su mano (DEBE ir antes de validar esCampoPropio)
        if (panelClickeado != null && panelClickeado.getParent() == panelMano) {
            if (cartaClickeada == null) {
                return;
            }

            // Si vuelve a tocar la misma carta Pokémon seleccionada, la deselecciona
            if (cartaClickeada == cartaSeleccionada) {
                cartaSeleccionada = null;
                limpiarBordesMano();
                registrarEvento("Deseleccionaste: " + cartaClickeada.getNombre());
                return;
            }

            entrenadorPendiente = null;
            cartaSeleccionada = null;
            energiaSeleccionada = false;
            limpiarBordesMano();

            // Caso A: Carta de Entrenador
            if (cartaClickeada instanceof Entrenador entrenador) {
                entrenadorPendiente = entrenador;
                panelClickeado.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 3));
                if (entrenador.requiereObjetivoTablero()) {
                    registrarEvento("Seleccionaste " + entrenador.getNombre() + ". Haz clic en un Pokémon de tu campo (o vuelve a tocarla para cancelar).");
                } else {
                    registrarEvento("Seleccionaste " + entrenador.getNombre() + ". Haz clic en el fondo para activarla (o vuelve a tocarla para cancelar).");
                }
                return;
            }

            // Caso B: Carta Pokémon (Básico o Evolución)
            String error = motorGrafico.seleccionarCartaMano(cartaClickeada);
            if (error != null) {
                JOptionPane.showMessageDialog(Tablero.this, error);
                return;
            }
            cartaSeleccionada = cartaClickeada;
            panelClickeado.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 0), 3));
            registrarEvento("Seleccionaste: " + cartaClickeada.getNombre());
            return;
        }

        // 5. Si tocó el fondo o cualquier panel fuera de tu campo (ej. rival), deselecciona carta Pokémon
        boolean esCampoPropio = (panelClickeado == panelActivo
                || panelClickeado == panelBanco1
                || panelClickeado == panelBanco3);

        if (panelClickeado == null || !esCampoPropio) {
            if (cartaSeleccionada != null) {
                cartaSeleccionada = null;
                limpiarBordesMano();
                registrarEvento("Selección cancelada.");
            }
            return;
        }

        // 6. SEGUNDO CLIC: El jugador tocó Activo o Banca propia
        if (cartaSeleccionada != null) {
            boolean esActivo = (panelClickeado == panelActivo);
            String error = motorGrafico.jugarCartaEnCampo(cartaSeleccionada, esActivo, objetivo);
            if (error != null) {
                JOptionPane.showMessageDialog(Tablero.this, error);
                return;
            }
            registrarEvento("Jugaste: " + cartaSeleccionada.getNombre());
            cartaSeleccionada = null;
            limpiarBordesMano();
            actualizar();
            actualizarMano();
            return;
        }

        if (energiaSeleccionada) {
            if (objetivo == null) {
                JOptionPane.showMessageDialog(Tablero.this, "Selecciona un Pokémon en tu campo para adjuntar la energía.");
                return;
            }
            String error = motorGrafico.adjuntarEnergia(objetivo);
            if (error != null) {
                JOptionPane.showMessageDialog(Tablero.this, error);
                return;
            }
            energiaSeleccionada = false;
            registrarEvento("Energía adjuntada a " + objetivo.getNombre() + ".");
            actualizar();
            return;
        }

        if (panelClickeado == panelActivo && objetivo != null) {
            panelInformacion.cargarPokemon(objetivo);
            int x = (panelFondo.getWidth() - popup.getPreferredSize().width) / 2;
            int y = (panelFondo.getHeight() - popup.getPreferredSize().height) / 2;
            popup.show(panelFondo, x, y);
        } else {
            JOptionPane.showMessageDialog(Tablero.this, "Primero selecciona una carta de tu mano");
        }
    }
};

private void conectarEventos() {
    panelFondo.addMouseListener(clickTablero);
    panelMano.addMouseListener(clickTablero);
    panelActivo.addMouseListener(clickTablero);
    panelBanco1.addMouseListener(clickTablero);
    panelBanco3.addMouseListener(clickTablero);
    panelActivoRival.addMouseListener(clickTablero);
    panelBanco4.addMouseListener(clickTablero);
    panelBanco5.addMouseListener(clickTablero);

    jLabel19.addMouseListener(clicEnergia);
    jButton1.addActionListener(e -> pasarTurno());
    btnRendirse.addActionListener(e -> rendirse());
    panelInformacion.getBttnAtaque1().addActionListener(e -> ejecutarAtaque(0));
    panelInformacion.getBttnAtaque2().addActionListener(e -> ejecutarAtaque(1));
    panelInformacion.getBttnRetirar().addActionListener(e -> iniciarRetirada());
}
    private void actualizarManoRival() {
    Jugador rival = motorGrafico.jugadorRival();
    int anchoCarta = (int) Math.round(CARD_WIDTH * escalaCartas);
    int altoCarta = (int) Math.round(CARD_HEIGHT * escalaCartas);

    manoRival.removeAll();
    manoRival.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 4));
    manoRival.setOpaque(false);
    manoRival.setPreferredSize(new Dimension(800, altoCarta + 8));
    

    ImageIcon iconoReverso = null;
    if (imagenDorsoMazo != null) {
        Image imgEscalada = imagenDorsoMazo.getScaledInstance(anchoCarta, altoCarta, Image.SCALE_SMOOTH);
        iconoReverso = new ImageIcon(imgEscalada);
    }

    for (int i = 0; i < rival.getMano().size(); i++) {
        JLabel cartaOculta = new JLabel(iconoReverso);
        Dimension tamano = new Dimension(anchoCarta, altoCarta);
        cartaOculta.setPreferredSize(tamano);
        cartaOculta.setMinimumSize(tamano);
        cartaOculta.setMaximumSize(tamano);
        manoRival.add(cartaOculta);
    }

    manoRival.revalidate();
    manoRival.repaint();
}

    private void actualizar() {
        Jugador propio = motorGrafico.jugadorActual();
        Jugador rival =  motorGrafico.jugadorRival();

        panelActivo.mostrar(propio.getCampo().getPokemonActivo());
        panelActivoRival.mostrar(rival.getCampo().getPokemonActivo());
        mostrarBanca(propio.getCampo().getBanco(), panelBanco3, panelBanco1);
        mostrarBanca(rival.getCampo().getBanco(), panelBanco5, panelBanco4);
        actualizarManoRival();

        jLabel17.setText("Rival: " + rival.getNombre());
        jLabel18.setText("Mi nombre: " + propio.getNombre());
        jLabel20.setText("Turno " + motorGrafico.getTurno() + " - " + propio.getNombre());
        jLabel7.setText("Premios: " + rival.getPuntosPremio() + "/3");
        jLabel8.setText("Premios: " + propio.getPuntosPremio() + "/3");
        jLabel10.setText("Número de cartas: " + rival.getMazo().getCantidadCartas());
        jLabel9.setText("Número de cartas: " + propio.getMazo().getCantidadCartas());

        TipoEnergia disponible = propio.getEnergiaDisponible();
        IconosEnergia iconosEnergia = new IconosEnergia();

        if (disponible == null) {
            jLabel19.setIcon(null);
            jLabel19.setText("Sin energía este turno");
            jLabel19.setBorder(null);
            jLabel19.setToolTipText("La energía se genera al comenzar tu turno.");
        } else {
            java.net.URL recursoEnergia = getClass().getResource(iconosEnergia.getRutaEnergia(disponible));
            jLabel19.setText("Clic para usar");
            jLabel19.setIcon(recursoEnergia == null ? null : new ImageIcon(recursoEnergia));
            jLabel19.setToolTipText("Haz clic para seleccionar esta energía y luego en uno de tus Pokémon.");
            jLabel19.setBorder(energiaSeleccionada
                    ? BorderFactory.createLineBorder(new Color(255, 204, 0), 2, true)
                    : null);
        }

        ajustarTamanosCartas();
    }

    private void mostrarBanca(java.util.List<Pokemon> banco, PokemonPanel primero, PokemonPanel segundo) {
        primero.mostrar(banco.size() > 0 ? banco.get(0) : null);
        segundo.mostrar(banco.size() > 1 ? banco.get(1) : null);
    }

};
