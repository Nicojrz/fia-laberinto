package laberinto;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public class PanelLaberinto extends JPanel {

    private int[][] matriz;
    private final Set<Long> explorados = new HashSet<>();
    private final Set<Long> camino = new HashSet<>();

    public PanelLaberinto() {
        setBackground(Color.WHITE);
        matriz = new int[Laberinto.TAMANIO][Laberinto.TAMANIO];
    }

    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
        limpiarBusqueda();
    }

    public void limpiarBusqueda() {
        explorados.clear();
        camino.clear();
        repaint();
    }

    public void marcarExplorado(int fila, int columna) {
        explorados.add(clave(fila, columna));
        repaint();
    }

    public void marcarCamino(int fila, int columna) {
        camino.add(clave(fila, columna));
        repaint();
    }

    private long clave(int fila, int columna) {
        return (long) fila * Laberinto.TAMANIO + columna;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int lado = Math.min(getWidth(), getHeight());
        int tam = lado / Laberinto.TAMANIO;
        if (tam <= 0) {
            return;
        }

        for (int i = 0; i < Laberinto.TAMANIO; i++) {
            for (int j = 0; j < Laberinto.TAMANIO; j++) {
                int x = j * tam;
                int y = i * tam;

                g.setColor(colorDeCasilla(i, j));
                g.fillRect(x, y, tam, tam);
            }
        }
    }

    private Color colorDeCasilla(int i, int j) {
        if (matriz[i][j] == 1) {
            return new Color(40, 40, 40);
        }
        if (i == Laberinto.INICIO[0] && j == Laberinto.INICIO[1]) {
            return new Color(0, 160, 0);
        }
        if (i == Laberinto.FIN[0] && j == Laberinto.FIN[1]) {
            return new Color(200, 0, 0);
        }
        if (camino.contains(clave(i, j))) {
            return new Color(255, 190, 0);
        }
        if (explorados.contains(clave(i, j))) {
            return new Color(190, 220, 255);
        }
        return Color.WHITE;
    }
}