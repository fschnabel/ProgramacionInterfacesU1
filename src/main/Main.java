
package main;

import vista.ventana;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new ventana().setVisible(true);
        });
    }
}
