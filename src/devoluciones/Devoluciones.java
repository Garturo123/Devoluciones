package devoluciones;

import javax.swing.SwingUtilities;

public class Devoluciones {

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Interface().setVisible(true);
        });
    }
    
}
