package laberinto;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BusquedaProfundidadLaberinto {

    private int nodosExplorados;
    private List<NodoLaberinto> ordenExploracion;

    public int getNodosExplorados() { return nodosExplorados; }
    public List<NodoLaberinto> getOrdenExploracion() { return ordenExploracion; }

    /** Busca un camino desde inicio hasta fin (no necesariamente el más corto). */
    public List<NodoLaberinto> buscar(int[][] matriz, int[] inicio, int[] fin) {
        nodosExplorados = 0;
        ordenExploracion = new ArrayList<>();

        boolean[][] visitado = new boolean[Laberinto.TAMANIO][Laberinto.TAMANIO];
        Deque<NodoLaberinto> pila = new ArrayDeque<>();

        NodoLaberinto raiz = new NodoLaberinto(inicio[0], inicio[1]);
        pila.push(raiz);
        visitado[raiz.getFila()][raiz.getColumna()] = true;

        while (!pila.isEmpty()) {
            NodoLaberinto actual = pila.pop();
            nodosExplorados++;
            ordenExploracion.add(actual);

            if (actual.getFila() == fin[0] && actual.getColumna() == fin[1]) {
                return actual.reconstruirCamino();
            }

            for (int d = 0; d < 4; d++) {
                int nf = actual.getFila() + Laberinto.DELTA_FILA[d];
                int nc = actual.getColumna() + Laberinto.DELTA_COLUMNA[d];

                if (!Laberinto.esTransitable(matriz, nf, nc) || visitado[nf][nc]) {
                    continue;
                }

                visitado[nf][nc] = true;
                pila.push(new NodoLaberinto(nf, nc, actual, Laberinto.DIRECCIONES[d]));
            }
        }

        return null;
    }
}