/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoia;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author jdtorres
 */
public class Principal implements ActionListener {

    JFrame frame;
    JPanel pGlobal, pMapa, pSolucion;
    JMenuBar menuBar;
    JMenu menuMapa, menuAlgoritmo;
    JMenuItem menuCargarMapa, menuInformada, menuNoInformada;
    ButtonGroup group;
    JRadioButtonMenuItem AlgAmplitud, AlgCostoUniforme, AlgProfundidadCicloNo,
            AlgAvara, AlgA;
    JButton btEjecutar, btSimular;
    JLabel lbSolucion;
    private static Mapa mp;
    public static HashMap<Point, JLabel> Labels;
    public static Point Agente;
    public static Point Meta;
    public static String Message;
    ArrayList<String> solucion = new ArrayList<>(); //ojo con esto
    int init_pos_x;
    int init_pos_y;
    File fichero;

    final public ImageIcon MARIO_IMG = scalar_imagen("/Imagenes/Mario.png");
    final public ImageIcon LADRILLO_IMG = scalar_imagen("/Imagenes/Ladrillo.png");
    final public ImageIcon TORTUGA_IMG = scalar_imagen("/Imagenes/Tortuga.png");
    final public ImageIcon PRINCESA_IMG = scalar_imagen("/Imagenes/Princesa.png");
    final public ImageIcon FLOR_IMG = scalar_imagen("/Imagenes/Flor.png");

    public static void main(String[] args) {
        new Principal().init();
    }

    public void init() {

        //Creacion de Panels
        frame = new JFrame();
        frame.setLayout(new MigLayout());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 700));
        frame.setResizable(false);
        pGlobal = new JPanel(new MigLayout());
        frame.add(pGlobal, "span, width max(100%, 100%) ");

        //Creación de menu
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        menuMapa = new JMenu("Mapa");
        menuCargarMapa = new JMenuItem("Cargar Mapa");
        menuMapa.add(menuCargarMapa);
        menuBar.add(menuMapa);

        menuAlgoritmo = new JMenu("Algoritmos");
        menuBar.add(menuAlgoritmo);
        menuInformada = new JMenuItem("Busqueda no Informada");
        menuInformada.setEnabled(false);
        menuAlgoritmo.add(menuInformada);
        group = new ButtonGroup();
        AlgAmplitud = new JRadioButtonMenuItem("Amplitud");
        group.add(AlgAmplitud);
        menuAlgoritmo.add(AlgAmplitud);
        AlgCostoUniforme = new JRadioButtonMenuItem("Costo uniforme");
        group.add(AlgCostoUniforme);
        menuAlgoritmo.add(AlgCostoUniforme);
        AlgProfundidadCicloNo = new JRadioButtonMenuItem("Profundidad E. C.");
        group.add(AlgProfundidadCicloNo);
        menuAlgoritmo.add(AlgProfundidadCicloNo);
        menuAlgoritmo.addSeparator();
        menuNoInformada = new JMenuItem("Busqueda Informada");
        menuNoInformada.setEnabled(false);
        menuAlgoritmo.add(menuNoInformada);
        AlgAvara = new JRadioButtonMenuItem("Avara");
        group.add(AlgAvara);
        menuAlgoritmo.add(AlgAvara);
        AlgA = new JRadioButtonMenuItem("A*");
        group.add(AlgA);
        menuAlgoritmo.add(AlgA);

        //Creacion panel mapa
        pMapa = new JPanel();
        pMapa.setLayout(new GridLayout());
        pMapa.setVisible(true);
        pMapa.setPreferredSize(new Dimension(600, 600));
        pMapa.setBorder(BorderFactory.createTitledBorder("Mapa"));
        pGlobal.add(pMapa, "growx, growy");

        //Panel solucion
        pSolucion = new JPanel(new MigLayout());
        pSolucion.setVisible(true);
        btEjecutar = new JButton("Ejecutar");
        btSimular = new JButton("Simular");
        lbSolucion = new JLabel();
        pSolucion.add(btEjecutar, "");
        pSolucion.add(btSimular, "wrap");
        pSolucion.add(lbSolucion, "span 4,wrap");
        pSolucion.setBorder(BorderFactory.createTitledBorder("Solución"));
        pGlobal.add(pSolucion, "growx, growy, width max(40%, 40%)");

        menuCargarMapa.addActionListener(this);
        btEjecutar.addActionListener(this);
        btSimular.addActionListener(this);
        frame.pack();
    }

    public ImageIcon scalar_imagen(String url) {
        ImageIcon imgIcoUV = new ImageIcon(this.getClass().getResource(url));
        Image image = imgIcoUV.getImage();
        Image newimg = image.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == menuCargarMapa) {
            mp = new Mapa();
            cargar_archivo();
            construir_mapa(mp.getMapa());
            find_cur_pos(mp.getMapa());
        }
        if (e.getSource() == btEjecutar) {
            construir_mapa(mp.getMapa());
            lbSolucion.setText("");
            int nodosE = 0, profundidad = 0;
            Instant inicio = Instant.now();
            if (AlgAmplitud.isSelected()) {
                solucion.clear();
                BusquedaNoInformada_Amplitud bnia = new BusquedaNoInformada_Amplitud(mp.getMapa());
                bnia.init();
                solucion = bnia.getSolucion();
                nodosE = bnia.getNodosE();
                profundidad = bnia.getProfundidad();
                System.out.println(bnia.getNodosE());
            } else if (AlgCostoUniforme.isSelected()) {
                solucion.clear();
                BusquedaNoInformada_CostoUniforme bnicu = new BusquedaNoInformada_CostoUniforme(mp.getMapa());
                bnicu.init();
                solucion = bnicu.getSolucion();
                nodosE = bnicu.getNodosE();
                profundidad = bnicu.getProfundidad();
                System.out.println(solucion.size());
            } else if (AlgProfundidadCicloNo.isSelected()) {
                solucion.clear();
                BusquedaNoInformada_Profundidad bnip = new BusquedaNoInformada_Profundidad(mp.getMapa());
                bnip.init();
                solucion = bnip.getSolucion();
                nodosE = bnip.getNodosE();
                profundidad = bnip.getProfundidad();
                System.out.println(solucion.size());
            } else if (AlgAvara.isSelected()) {
                solucion.clear();
                BusquedaInformada_Heuristica bih = new BusquedaInformada_Heuristica(mp.getMapa());
                bih.init();
                solucion = bih.getSolucion();
                nodosE = bih.getNodosE();
                profundidad = bih.getProfundidad();
                System.out.println(solucion.size());
            } else if (AlgA.isSelected()) {
                solucion.clear();
                BusquedaInformada_AEstrella biae = new BusquedaInformada_AEstrella(mp.getMapa());
                biae.init();
                solucion = biae.getSolucion();
                nodosE = biae.getNodosE();
                profundidad = biae.getProfundidad();
                System.out.println(solucion.size());
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione algoritmo de búsqueda");
                return;
            }
            Instant fin = Instant.now();
            Message = "<html><span style='font-size:1.2em' >Tiempo de cómputo : </span>"
                    + Duration.between(inicio, fin).toMillis() + " Milisegundos <br>"
                    + "<span style='font-size:1.2em' >Profundidad del arbol : </span> "
                    + profundidad + "<br>"
                    + "<span style='font-size:1.2em' >Nodos expandidos : </span>"
                    + nodosE + "<br>"
                    //añadir nodos expandidos y profundidad arbol
                    + "</html>";
            System.out.println(Message);
            lbSolucion.setText(Message);
            mp.archivo_mapa(fichero);
        }
        if (e.getSource() == btSimular) {
            construir_mapa(mp.getMapa());
            simular();
        }
    }

    public void simular() {

        final int delay = 300;
        //mostrar_arbol();
        new Timer(delay, new ActionListener() {
            //Point posicion = agente.getPosicion();
            final Iterator<String> camino = solucion.iterator();
            String s;
            Icon icon = null;
            int posf = init_pos_x;
            int posc = init_pos_y;
            Point p = new Point(posc, posf);

            public void actionPerformed(ActionEvent e) {
                if (camino.hasNext()) {
                    JLabel lblposicion = Labels.get(p);
                    lblposicion.setIcon(icon);
                    lblposicion.setBackground(Color.GREEN);
                    s = camino.next();
                    //System.out.println(s);
                    if (s.equals("up")) {

                        posf--;
                        p.setLocation(posc, posf);
                    } else if (s.equals("down")) {

                        posf++;
                        p.setLocation(posc, posf);
                    } else if (s.equals("rigth")) { //Corregir right

                        posc++;
                        p.setLocation(posc, posf);
                    } else if (s.equals("left")) {

                        posc--;
                        p.setLocation(posc, posf);
                    }

                    //lblposicion.setText(p.getCosto() + ":" + p.getHeuristica());
                    JLabel PosicionNueva = Labels.get(p);
                    icon = PosicionNueva.getIcon();

                    PosicionNueva.setIcon(MARIO_IMG);
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();

        /*
        int[][] maux = new int[10][10];
        int[][] mx = mp.getMapa();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                maux[i][j] = mx[i][j];
            }
        }

        int posf = init_pos_x;
        int posc = init_pos_y;

        int delay = 300;
        
        for (int i = 0; i < solucion.size(); i++) {
            String mov = solucion.get(i);
            System.out.println(mov);
            if (mov.equals("up")) {

                maux[posf - 1][posc] = 2;
                if (mp.getPos(posf, posc) == 2) {

                    maux[posf][posc] = 0;

                } else {

                    maux[posf][posc] = mp.getPos(posf, posc);

                }
                posf--;
            } else if (mov.equals("down")) {
                maux[posf + 1][posc] = 2;
                if (mp.getPos(posf, posc) == 2) {
                    maux[posf][posc] = 0;
                } else {
                    maux[posf][posc] = mp.getPos(posf, posc);
                }
                posf++;
            } else if (mov.equals("rigth")) { //Corregir right
                maux[posf][posc + 1] = 2;
                if (mp.getPos(posf, posc) == 2) {
                    maux[posf][posc] = 0;
                } else {
                    maux[posf][posc] = mp.getPos(posf, posc);
                }
                posc++;
            } else if (mov.equals("left")) {
                maux[posf][posc - 1] = 2;
                if (mp.getPos(posf, posc) == 2) {
                    maux[posf][posc] = 0;
                } else {
                    maux[posf][posc] = mp.getPos(posf, posc);
                }
                posc--;
            }

            construir_mapa(maux);
            
        
        }

        System.out.println("Fin simulacion");
         */
    }

    void find_cur_pos(int maze[][]) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == 2) {
                    init_pos_x = i;
                    init_pos_y = j;
                    return;
                }

            }

        }
    }

    public void cargar_archivo() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        JFileChooser selectorArchivos = new JFileChooser(System.getProperty("user.dir") + "/mapas");
        selectorArchivos.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.TXT", "txt");
        selectorArchivos.addChoosableFileFilter(filter);
        selectorArchivos.setAcceptAllFileFilterUsed(false);
        int seleccion = selectorArchivos.showOpenDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            fichero = selectorArchivos.getSelectedFile();
            mp.archivo_mapa(fichero);
        }
    }

    public void construir_mapa(int[][] mapa) {
        pMapa.removeAll();
        pMapa.updateUI();
        pMapa.setLayout(new GridLayout(mapa.length, mapa[0].length));
        JLabel lb;
        Labels = new HashMap<>();
        for (int i = 0; i < mapa.length; i++) {//y
            for (int j = 0; j < mapa[0].length; j++) {//x
                lb = new JLabel();
                lb.setOpaque(true);
                switch (mapa[i][j]) {
                    case Mapa.LIBRE:
                        break;
                    case 1:
                        lb.setIcon(LADRILLO_IMG);
                        break;
                    case Mapa.INICIO:
                        lb.setIcon(MARIO_IMG);
                        Agente = new Point(j, i);
                        break;
                    case Mapa.FLOR:
                        lb.setIcon(FLOR_IMG);
                        break;
                    case Mapa.TORTUGA:

                        lb.setIcon(TORTUGA_IMG);
                        break;
                    case Mapa.PRINCESA:
                        Meta = new Point(j, i);
                        lb.setIcon(PRINCESA_IMG);
                        break;
                }
                Dimension d = new Dimension(pMapa.getWidth() / mapa.length, pMapa.getWidth() / mapa.length);
                lb.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                lb.setHorizontalAlignment(JLabel.CENTER);
                lb.setMaximumSize(d);
                Labels.put(new Point(j, i), lb);
                pMapa.add(lb);
            }
        }
        pMapa.updateUI();
    }
}
