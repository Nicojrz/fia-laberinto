package laberinto;

import java.util.ArrayList;
import java.util.List;

public class NodoLaberinto {

    private final int fila;
    private final int columna;
    private final NodoLaberinto anterior;
    private final String movimiento;
    private final int profundidad;

    public NodoLaberinto(int fila, int columna) {
        this(fila, columna, null, null);
    }

    public NodoLaberinto(int fila, int columna, NodoLaberinto anterior, String movimiento) {
        this.fila = fila;
        this.columna = columna;
        this.anterior = anterior;
        this.movimiento = movimiento;
        this.profundidad = (anterior != null) ? anterior.profundidad + 1 : 0;
    }

    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public NodoLaberinto getAnterior() { return anterior; }
    public String getMovimiento() { return movimiento; }
    public int getProfundidad() { return profundidad; }

    public List<NodoLaberinto> reconstruirCamino() {
        List<NodoLaberinto> camino = new ArrayList<>();
        NodoLaberinto actual = this;
        while (actual != null) {
            camino.add(0, actual);
            actual = actual.anterior;
        }
        return camino;
    }
}