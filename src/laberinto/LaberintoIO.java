package laberinto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

public class LaberintoIO {

    public static int[][] cargarDesdeArchivo(File archivo) throws IOException {
        List<String> lineas = Files.readAllLines(archivo.toPath());
        while (!lineas.isEmpty() && lineas.get(lineas.size() - 1).trim().isEmpty()) {
            lineas.remove(lineas.size() - 1);
        }

        if (lineas.size() != Laberinto.TAMANIO) {
            throw new IOException("El archivo debe tener exactamente " + Laberinto.TAMANIO
                    + " filas (tiene " + lineas.size() + ").");
        }

        int[][] matriz = new int[Laberinto.TAMANIO][Laberinto.TAMANIO];
        for (int i = 0; i < Laberinto.TAMANIO; i++) {
            String limpia = lineas.get(i).replaceAll("[^01]", "");
            if (limpia.length() != Laberinto.TAMANIO) {
                throw new IOException("La fila " + (i + 1) + " debe tener " + Laberinto.TAMANIO
                        + " valores de 0/1 (tiene " + limpia.length() + ").");
            }
            for (int j = 0; j < Laberinto.TAMANIO; j++) {
                matriz[i][j] = limpia.charAt(j) - '0';
            }
        }

        int[] ini = Laberinto.INICIO;
        int[] fin = Laberinto.FIN;
        if (matriz[ini[0]][ini[1]] != 0 || matriz[fin[0]][fin[1]] != 0) {
            throw new IOException("Las casillas de inicio (" + ini[0] + "," + ini[1] + ") y fin ("
                    + fin[0] + "," + fin[1] + ") deben valer 0 (ser transitables).");
        }

        return matriz;
    }

    public static void guardarAArchivo(File archivo, int[][] matriz) throws IOException {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(archivo))) {
            for (int[] fila : matriz) {
                StringBuilder sb = new StringBuilder();
                for (int valor : fila) {
                    sb.append(valor);
                }
                escritor.println(sb);
            }
        }
    }
}