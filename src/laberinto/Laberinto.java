package laberinto;

public class Laberinto {

    public static final int TAMANIO = 50;

    public static final String[] DIRECCIONES = {"ARRIBA", "ABAJO", "IZQUIERDA", "DERECHA"};
    public static final int[] DELTA_FILA    = {-1, 1, 0, 0};
    public static final int[] DELTA_COLUMNA = {0, 0, -1, 1};

    public static final int[] INICIO = {0, 0};
    public static final int[] FIN = {TAMANIO - 1, TAMANIO - 1};

    public static boolean esTransitable(int[][] matriz, int fila, int columna) {
        return fila >= 0 && fila < TAMANIO
                && columna >= 0 && columna < TAMANIO
                && matriz[fila][columna] == 0;
    }
}