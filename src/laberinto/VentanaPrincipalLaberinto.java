package laberinto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaPrincipalLaberinto extends JFrame {

    private final PanelLaberinto panel;
    private final JButton btnGenerar;
    private final JButton btnCargar;
    private final JButton btnGuardar;
    private final JButton btnBfs;
    private final JButton btnDfs;
    private final JLabel lblEstado;

    private int[][] laberintoActual;

    public VentanaPrincipalLaberinto() {
        super("Laberinto " + Laberinto.TAMANIO + "x" + Laberinto.TAMANIO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        laberintoActual = new GeneradorLaberinto().generar();

        panel = new PanelLaberinto();
        panel.setMatriz(laberintoActual);
        panel.setPreferredSize(new Dimension(600, 600));

        btnGenerar = new JButton("Generar laberinto");
        btnCargar = new JButton("Cargar archivo...");
        btnGuardar = new JButton("Guardar archivo...");
        btnBfs = new JButton("Resolver (BFS)");
        btnDfs = new JButton("Resolver (DFS)");
        lblEstado = new JLabel("Laberinto generado. Verde = inicio, rojo = fin.");

        btnGenerar.addActionListener(e -> generar());
        btnCargar.addActionListener(e -> cargar());
        btnGuardar.addActionListener(e -> guardar());
        btnBfs.addActionListener(e -> resolver(true));
        btnDfs.addActionListener(e -> resolver(false));

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        controles.add(btnGenerar);
        controles.add(btnCargar);
        controles.add(btnGuardar);
        controles.add(btnBfs);
        controles.add(btnDfs);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(controles, BorderLayout.SOUTH);
        add(lblEstado, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void generar() {
        laberintoActual = new GeneradorLaberinto().generar();
        panel.setMatriz(laberintoActual);
        lblEstado.setText("Nuevo laberinto generado.");
    }

    private void cargar() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona el archivo con la matriz (0/1, " + Laberinto.TAMANIO + " filas x "
                + Laberinto.TAMANIO + " columnas)");
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            laberintoActual = LaberintoIO.cargarDesdeArchivo(chooser.getSelectedFile());
            panel.setMatriz(laberintoActual);
            lblEstado.setText("Laberinto cargado desde archivo.");
        } catch (Exception ex) {
            lblEstado.setText("Error al cargar: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar matriz del laberinto actual");
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            LaberintoIO.guardarAArchivo(chooser.getSelectedFile(), laberintoActual);
            lblEstado.setText("Laberinto guardado.");
        } catch (Exception ex) {
            lblEstado.setText("Error al guardar: " + ex.getMessage());
        }
    }

    private void establecerControlesHabilitados(boolean habilitado) {
        btnGenerar.setEnabled(habilitado);
        btnCargar.setEnabled(habilitado);
        btnGuardar.setEnabled(habilitado);
        btnBfs.setEnabled(habilitado);
        btnDfs.setEnabled(habilitado);
    }

    private void resolver(boolean usarBfs) {
        establecerControlesHabilitados(false);
        panel.limpiarBusqueda();
        lblEstado.setText(usarBfs ? "Resolviendo con BFS..." : "Resolviendo con DFS...");

        final int[][] matrizParaResolver = laberintoActual;

        SwingWorker<List<NodoLaberinto>, Void> worker = new SwingWorker<List<NodoLaberinto>, Void>() {
            private int nodosExplorados;
            private List<NodoLaberinto> orden;
            private long tiempoMs;

            @Override
            protected List<NodoLaberinto> doInBackground() {
                long inicio = System.currentTimeMillis();
                List<NodoLaberinto> camino;
                if (usarBfs) {
                    BusquedaAnchuraLaberinto bfs = new BusquedaAnchuraLaberinto();
                    camino = bfs.buscar(matrizParaResolver, Laberinto.INICIO, Laberinto.FIN);
                    nodosExplorados = bfs.getNodosExplorados();
                    orden = bfs.getOrdenExploracion();
                } else {
                    BusquedaProfundidadLaberinto dfs = new BusquedaProfundidadLaberinto();
                    camino = dfs.buscar(matrizParaResolver, Laberinto.INICIO, Laberinto.FIN);
                    nodosExplorados = dfs.getNodosExplorados();
                    orden = dfs.getOrdenExploracion();
                }
                tiempoMs = System.currentTimeMillis() - inicio;
                return camino;
            }

            @Override
            protected void done() {
                try {
                    List<NodoLaberinto> camino = get();
                    animarExploracionYCamino(orden, camino, nodosExplorados, tiempoMs);
                } catch (Exception ex) {
                    lblEstado.setText("Error durante la búsqueda: " + ex.getMessage());
                    establecerControlesHabilitados(true);
                }
            }
        };
        worker.execute();
    }

    private void animarExploracionYCamino(List<NodoLaberinto> orden, List<NodoLaberinto> camino,
                                           int nodosExplorados, long tiempoMs) {
        final int[] indice = {0};
        // varias celdas por fotograma para que la animación no tarde demasiado
        final int celdasPorFotograma = Math.max(1, orden.size() / 200);
        Timer timerExploracion = new Timer(5, null);

        timerExploracion.addActionListener(e -> {
            int limite = Math.min(indice[0] + celdasPorFotograma, orden.size());
            for (int i = indice[0]; i < limite; i++) {
                NodoLaberinto n = orden.get(i);
                panel.marcarExplorado(n.getFila(), n.getColumna());
            }
            indice[0] = limite;

            if (indice[0] >= orden.size()) {
                timerExploracion.stop();
                if (camino == null || camino.isEmpty()) {
                    lblEstado.setText("No se encontró camino hasta la salida. Nodos explorados: " + nodosExplorados);
                    establecerControlesHabilitados(true);
                } else {
                    animarCamino(camino, nodosExplorados, tiempoMs);
                }
            }
        });
        timerExploracion.start();
    }

    private void animarCamino(List<NodoLaberinto> camino, int nodosExplorados, long tiempoMs) {
        final int[] indice = {0};
        Timer timer = new Timer(15, null);

        timer.addActionListener(e -> {
            if (indice[0] >= camino.size()) {
                timer.stop();
                lblEstado.setText("Camino encontrado: " + (camino.size() - 1) + " pasos. "
                        + "Nodos explorados: " + nodosExplorados + ". Tiempo: " + tiempoMs + " ms.");
                establecerControlesHabilitados(true);
                return;
            }
            NodoLaberinto n = camino.get(indice[0]);
            panel.marcarCamino(n.getFila(), n.getColumna());
            indice[0]++;
        });
        timer.start();
    }
}