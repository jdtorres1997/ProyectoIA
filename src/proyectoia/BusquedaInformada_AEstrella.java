/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoia;

import java.util.*;
import proyectoia.Nodo.*;
import java.math.*;

/**
 *
 * @author juan
 */
public class BusquedaInformada_AEstrella {

    int target_pos_x;
    int target_pos_y;
    int init_pos_x;
    int init_pos_y;
    static int maze[][];
    static private ArrayList<String> solucion;

    private static int nodosE = 0;
    private static int profundidad = 0;
    
    BusquedaInformada_AEstrella(int[][] mapa) {
        maze = mapa;
        solucion = new ArrayList<>();
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

    void find_target_pos(int maze[][]) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == 5) {
                    target_pos_x = i;
                    target_pos_y = j;
                    return;
                }

            }

        }
    }

    String find_move(int maze[][], int cur_pos_x, int cur_pos_y) {
        String sol = "";
        if (cur_pos_x != 0) {
            if (maze[cur_pos_x - 1][cur_pos_y] != 1) {
                sol += "8";
            }
        }
        if (cur_pos_x != maze.length - 1) {
            if (maze[cur_pos_x + 1][cur_pos_y] != 1) {
                sol += "2";
            }
        }
        if (cur_pos_y != 0) {
            if (maze[cur_pos_x][cur_pos_y - 1] != 1) {
                sol += "4";
            }
        }
        if (cur_pos_y != maze[0].length - 1) {
            if (maze[cur_pos_x][cur_pos_y + 1] != 1) {
                sol += "6";
            }
        }
        return sol;
    }

    void traceRoute(Vector<Nodo> camino) {
        int size = camino.size();
        int id = camino.get(size - 1).getId();
        String route = "";
        ArrayList<String> temp = new ArrayList<>();
        for (int i = size - 1; i >= 0; i--) {
            if (camino.get(i).getId() == id && camino.get(i).getId() != 0) {
                id = camino.get(i).getParent_id();
                route = camino.get(i).getMov() + "\n" + route;
                temp.add(camino.get(i).getMov());
            }
        }
        for (int i = (temp.size() - 1); i >= 0; i--) {
            solucion.add(temp.get(i));
        }
        System.out.print(route);

    }
    
    
    ArrayList<String> getSolucion() {
        return solucion;
    }
    public int getProfundidad() {
        return profundidad;
    }

    public static void setProfundidad(int profundidad) {
        BusquedaInformada_AEstrella.profundidad = profundidad;
    }

    public int getNodosE(){
        return nodosE;
    }

    int manhatan_Distance(int pos_x, int pos_y) {
        int respuesta = 0;
        int diferencia_x = pos_x - target_pos_x;
        int diferencia_y = pos_y - target_pos_y;
        respuesta = Math.abs(diferencia_x) + Math.abs(diferencia_y);
        return respuesta;
    }

    Vector organizar(Vector<Nodo> arreglo, Nodo new_nodo, int pos_ini, int pos_fin) {
        int pos_busqueda = (int) ((pos_ini + pos_fin) / 2);
        if (pos_ini != pos_fin && arreglo.size() > 1) {
            //System.out.println(arreglo.size());
            if (new_nodo.getProductividad() >= arreglo.get(pos_busqueda).getProductividad()) {
                if (pos_ini == pos_fin - 1) {
                    arreglo.add(pos_fin, new_nodo);
                } else {
                    organizar(arreglo, new_nodo, pos_busqueda, pos_fin);
                }
            } else {
                organizar(arreglo, new_nodo, pos_ini, pos_busqueda);
            }
        } else if (arreglo.size() > 1) {
            arreglo.add(pos_busqueda, new_nodo);
        } else if (arreglo.isEmpty()) {
            arreglo.add(new_nodo);
        } else if (arreglo.get(0).getProductividad() > new_nodo.getProductividad()) {
            arreglo.add(0, new_nodo);
        } else {
            arreglo.add(new_nodo);
        }

        return arreglo;
    }

    int cost(Vector<Nodo> arreglo, Nodo new_nodo, int[][] maze) {
        int solucion = 0;
        if (new_nodo.getEstado() == 0 && maze[new_nodo.getCur_pos_x()][new_nodo.getCur_pos_y()] == 4) {
            solucion = 10;
        } else {
            solucion = 1;
        }
        for (int i = arreglo.size() - 1; i >= 0; i--) {
            if (arreglo.get(i).getId() == new_nodo.getParent_id()) {
                solucion += arreglo.get(i).getCoste();
                return solucion;
            }
        }
        return solucion;
    }

    /**
     * @param args the command line arguments
     */
    public static void init() {
        System.err.println("busqueda informada por a estrella");
        BusquedaInformada_AEstrella aux = new BusquedaInformada_AEstrella(maze);
        int id = 0;
        Nodo nod = new Nodo(id, id);
        Vector<Nodo> arbol = new Vector();
        Vector<Nodo> hojas = new Vector();
        Vector<Nodo> hijos = new Vector();
        hojas.add(nod);
        aux.find_cur_pos(maze);
        aux.find_target_pos(maze);
        maze[aux.init_pos_x][aux.init_pos_y] = 0;
        hojas.get(0).setCur_pos_x(aux.init_pos_x);
        hojas.get(0).setCur_pos_y(aux.init_pos_y);
        System.out.println("Initial posicion: (" + aux.init_pos_x + "," + aux.init_pos_y + ")");
        System.out.println("Target posicion: (" + aux.target_pos_x + "," + aux.target_pos_y + ")");
        Boolean flag = true;
        boolean matica = false;
        do {
            matica = false;
            if (hojas.get(0).getCur_pos_x() == aux.target_pos_x && hojas.get(0).getCur_pos_y() == aux.target_pos_y) {
                flag = false;
                arbol.add(hojas.get(0));
                nodosE++;
                
            }
            if (flag) {
                if (maze[hojas.get(0).getCur_pos_x()][hojas.get(0).getCur_pos_y()] == 3 && hojas.get(0).getEstado() != 1) {
                    hojas.get(0).setEstado(1);
                    matica = true;
                }
                String moves = aux.find_move(maze, hojas.get(0).getCur_pos_x(), hojas.get(0).getCur_pos_y());
                if (moves.contains("8") && (hojas.get(0).getMov() != "down" || matica)) {
                    id++;
                    Nodo new_son = new Nodo(id, hojas.get(0).getId());
                    new_son.setCur_pos_x(hojas.get(0).getCur_pos_x() - 1);
                    new_son.setCur_pos_y(hojas.get(0).getCur_pos_y());
                    new_son.setMov("up");
                    new_son.setEstado(hojas.get(0).getEstado());
                    new_son.setCoste(aux.cost(arbol, new_son, maze));
                    new_son.setProductividad(aux.manhatan_Distance(new_son.getCur_pos_x(), new_son.getCur_pos_y()) + new_son.getCoste());
                    new_son.setProfundidad(hojas.get(0).getProfundidad()+1);
                    if(new_son.getProfundidad() > aux.getProfundidad()){
                        aux.setProfundidad(new_son.getProfundidad());
                    }
                    hijos.add(new_son);
                }
                if (moves.contains("6") && (hojas.get(0).getMov() != "left" || matica)) {
                    id++;
                    Nodo new_son = new Nodo(id, hojas.get(0).getId());
                    new_son.setCur_pos_x(hojas.get(0).getCur_pos_x());
                    new_son.setCur_pos_y(hojas.get(0).getCur_pos_y() + 1);
                    new_son.setMov("rigth");
                    new_son.setEstado(hojas.get(0).getEstado());
                    new_son.setCoste(aux.cost(arbol, new_son, maze));
                    new_son.setProductividad(aux.manhatan_Distance(new_son.getCur_pos_x(), new_son.getCur_pos_y()) + new_son.getCoste());
                    new_son.setProfundidad(hojas.get(0).getProfundidad()+1);
                    if(new_son.getProfundidad() > aux.getProfundidad()){
                        aux.setProfundidad(new_son.getProfundidad());
                    }
                    hijos.add(new_son);
                }
                if (moves.contains("2") && (hojas.get(0).getMov() != "up" || matica)) {
                    id++;
                    Nodo new_son = new Nodo(id, hojas.get(0).getId());
                    new_son.setCur_pos_x(hojas.get(0).getCur_pos_x() + 1);
                    new_son.setCur_pos_y(hojas.get(0).getCur_pos_y());
                    new_son.setMov("down");
                    new_son.setEstado(hojas.get(0).getEstado());
                    new_son.setCoste(aux.cost(arbol, new_son, maze));
                    new_son.setProductividad(aux.manhatan_Distance(new_son.getCur_pos_x(), new_son.getCur_pos_y()) + new_son.getCoste());
                    new_son.setProfundidad(hojas.get(0).getProfundidad()+1);
                    if(new_son.getProfundidad() > aux.getProfundidad()){
                        aux.setProfundidad(new_son.getProfundidad());
                    }
                    hijos.add(new_son);
                }
                if (moves.contains("4") && (hojas.get(0).getMov() != "rigth" || matica)) {
                    id++;
                    Nodo new_son = new Nodo(id, hojas.get(0).getId());
                    new_son.setCur_pos_x(hojas.get(0).getCur_pos_x());
                    new_son.setCur_pos_y(hojas.get(0).getCur_pos_y() - 1);
                    new_son.setMov("left");
                    new_son.setEstado(hojas.get(0).getEstado());
                    new_son.setCoste(aux.cost(arbol, new_son, maze));
                    new_son.setProductividad(aux.manhatan_Distance(new_son.getCur_pos_x(), new_son.getCur_pos_y()) + new_son.getCoste());
                    new_son.setProfundidad(hojas.get(0).getProfundidad()+1);
                    if(new_son.getProfundidad() > aux.getProfundidad()){
                        aux.setProfundidad(new_son.getProfundidad());
                    }
                    hijos.add(new_son);
                }

                arbol.add(hojas.get(0));
                nodosE++;
                hojas.remove(0);
                for (int i = 0; i < hijos.size(); i++) {
                    hojas = aux.organizar(hojas, hijos.get(i), 0, hojas.size());
                }
                hijos.clear();

            }
        } while (flag);
        aux.traceRoute(arbol);
        /*
        String moves = aux.find_move(maze, nod.getCur_pos_x(), nod.getCur_pos_y());
        moves = moves.replaceAll("8", "up \n");
        moves = moves.replaceAll("4", "left \n");
        moves = moves.replaceAll("6", "rigth \n");
        moves = moves.replaceAll("2", "dawn \n");
        System.out.println("Mario can move:\n" + moves);
         */
    }

}
