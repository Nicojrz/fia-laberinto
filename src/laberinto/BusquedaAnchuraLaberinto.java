package laberinto;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class BusquedaAnchuraLaberinto {

    private int nodosExplorados;
    private List<NodoLaberinto> ordenExploracion;

    public int getNodosExplorados() { return nodosExplorados; }
    public List<NodoLaberinto> getOrdenExploracion() { return ordenExploracion; }

    /** Busca un camino desde inicio hasta fin. Devuelve null si no hay solución. */
    public List<NodoLaberinto> buscar(int[][] matriz, int[] inicio, int[] fin) {
        nodosExplorados = 0;
        ordenExploracion = new ArrayList<>();

        NodoLaberinto raiz = new NodoLaberinto(inicio[0], inicio[1]);
        if (raiz.getFila() == fin[0] && raiz.getColumna() == fin[1]) {
            return raiz.reconstruirCamino();
        }

        boolean[][] visitado = new boolean[Laberinto.TAMANIO][Laberinto.TAMANIO];
        Queue<NodoLaberinto> frontera = new ArrayDeque<>();
        frontera.add(raiz);
        visitado[raiz.getFila()][raiz.getColumna()] = true;

        while (!frontera.isEmpty()) {
            NodoLaberinto actual = frontera.poll();
            nodosExplorados++;
            ordenExploracion.add(actual);

            for (int d = 0; d < 4; d++) {
                int nf = actual.getFila() + Laberinto.DELTA_FILA[d];
                int nc = actual.getColumna() + Laberinto.DELTA_COLUMNA[d];

                if (!Laberinto.esTransitable(matriz, nf, nc) || visitado[nf][nc]) {
                    continue;
                }

                NodoLaberinto hijo = new NodoLaberinto(nf, nc, actual, Laberinto.DIRECCIONES[d]);

                if (nf == fin[0] && nc == fin[1]) {
                    return hijo.reconstruirCamino();
                }

                visitado[nf][nc] = true;
                frontera.add(hijo);
            }
        }

        return null;
    }
}