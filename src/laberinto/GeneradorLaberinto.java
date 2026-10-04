package laberinto;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class GeneradorLaberinto {

    public int[][] generar() {
        int tam = Laberinto.TAMANIO;
        int[][] matriz = new int[tam][tam];
        for (int[] fila : matriz) {
            Arrays.fill(fila, 1); // todo empieza como pared
        }

        int celdas = (tam - 1) / 2; // salas por lado (posiciones 0,2,...,2*(celdas-1))
        boolean[][] visitado = new boolean[celdas][celdas];
        Random random = new Random();

        Deque<int[]> pila = new ArrayDeque<>();
        visitado[0][0] = true;
        matriz[0][0] = 0;
        pila.push(new int[]{0, 0});

        while (!pila.isEmpty()) {
            int[] actual = pila.peek();
            List<Integer> direcciones = new ArrayList<>(List.of(0, 1, 2, 3));
            Collections.shuffle(direcciones, random);

            boolean avanzo = false;
            for (int d : direcciones) {
                int nf = actual[0] + Laberinto.DELTA_FILA[d];
                int nc = actual[1] + Laberinto.DELTA_COLUMNA[d];

                if (nf < 0 || nf >= celdas || nc < 0 || nc >= celdas || visitado[nf][nc]) {
                    continue;
                }

                // abre la pared entre la sala actual y la vecina, y la vecina misma
                int filaPared = actual[0] * 2 + Laberinto.DELTA_FILA[d];
                int columnaPared = actual[1] * 2 + Laberinto.DELTA_COLUMNA[d];
                matriz[filaPared][columnaPared] = 0;
                matriz[nf * 2][nc * 2] = 0;

                visitado[nf][nc] = true;
                pila.push(new int[]{nf, nc});
                avanzo = true;
                break;
            }

            if (!avanzo) {
                pila.pop();
            }
        }

        conectarEsquinaFinal(matriz, celdas);
        return matriz;
    }

    private void conectarEsquinaFinal(int[][] matriz, int celdas) {
        int tam = Laberinto.TAMANIO;
        int ultimaFila = (celdas - 1) * 2;
        int ultimaColumna = (celdas - 1) * 2;

        for (int i = ultimaFila; i < tam; i++) {
            matriz[i][ultimaColumna] = 0;
        }
        for (int j = ultimaColumna; j < tam; j++) {
            matriz[tam - 1][j] = 0;
        }
        matriz[tam - 1][tam - 1] = 0;
    }
}