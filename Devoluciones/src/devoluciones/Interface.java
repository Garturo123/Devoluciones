package devoluciones;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class Interface extends JFrame {
    public Interface() {
        setTitle("Devoluciones");
        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());

        JPanel nav = new JPanel();
        nav.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        nav.setBackground(new Color(240, 240, 240));
        nav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(100,100,100)));

        JButton btnConsulta = crearBotonNav("Consulta");
        JButton btnCrear = crearBotonNav("Crear");

        JButton[] botones = {btnConsulta, btnCrear};
        for (JButton b : botones) {
            b.setFocusPainted(false);
            b.setBackground(new Color(230, 230, 230));
            b.setForeground(Color.BLACK);
        }
        btnConsulta.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                recarga(panelPrincipal,nav);
                btnCrear.setBackground(new Color(230, 230, 230));
                btnConsulta.setBackground(new Color(200, 200, 200));
                new Consulta(panelPrincipal).setVisible(true);
               
            });
        });
        btnCrear.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                recarga(panelPrincipal,nav);
                btnCrear.setBackground(new Color(200, 200, 200));
                btnConsulta.setBackground(new Color(230, 230, 230));
                new Crear(panelPrincipal).setVisible(true);
                
            });
        });
        new Consulta(panelPrincipal).setVisible(true);
        nav.add(btnConsulta);
        nav.add(btnCrear);
        panelPrincipal.add(nav, BorderLayout.NORTH);
        add(panelPrincipal);
    }
    private JButton crearBotonNav(String texto) {
        JButton boton = new JButton(texto);

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(200,200,200));
            }

            public void mouseExited(MouseEvent e) {
                boton.setBackground(new Color(230,230,230));
            }
        });

        return boton;
    }

    void recarga(JPanel panelPrincipal, JPanel nav){
            panelPrincipal.removeAll();
            panelPrincipal.add(nav, BorderLayout.NORTH);
            panelPrincipal.revalidate();
            panelPrincipal.repaint();
        }

}
