package proyectoia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.swing.JOptionPane;

/**
 *
 * @author DevArt
 */
public class Mapa implements Cloneable {

    /**
     * 0 si es camino libre , 1 si es un obstáculo ,2 si el punto de inicio ,3
     * si es un robot enemigo , 4 si es el ítem *
     */
    public static final int LIBRE = 0;
    public static final int LADRILLO = 1;
    public static final int INICIO = 2;
    public static final int FLOR = 3;
    public static final int TORTUGA = 4;
    public static final int PRINCESA = 5;

    public static final int COSTO_NORMAL = 1;
    public static final int COSTO_ENEMIGO = 7;

    private int[][] mapa;

    public Mapa() {
    }

    public void archivo_mapa(File f) {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            mapa = br.lines()
                    .map(linea -> Arrays.stream(linea.split(" "))
                            .mapToInt(Integer::parseInt)
                            .toArray())
                    .toArray(int[][]::new);
            br.close();
            fr.close();
            for (int i = 0; i < mapa.length; i++) {
                String con = "";
                for (int j = 0; j < mapa[0].length; j++) {
                    con = con + " " + mapa[i][j];
                    
                }
                System.out.println(con);
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar mapa, valide el archivo de texto");
        }

    }

    public int[][] getMapa() {
        return mapa;
    }

}
