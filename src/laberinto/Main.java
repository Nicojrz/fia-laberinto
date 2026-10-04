package laberinto;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipalLaberinto ventana = new VentanaPrincipalLaberinto();
            ventana.setVisible(true);
        });
    }
}