package devoluciones;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;

public class Crear extends JPanel{
    
    public Crear(JPanel panel){
         
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        Dimension fieldSize = new Dimension(250, 30);

        // Codigo
        JPanel filaCodigo = new JPanel();
        filaCodigo.setLayout(new BoxLayout(filaCodigo, BoxLayout.X_AXIS));

        JLabel labelCodigo = new JLabel("No. Factura: ");
        JTextField codigo = new JTextField();
        codigo.setMaximumSize(fieldSize);

        filaCodigo.add(labelCodigo);
        filaCodigo.add(Box.createHorizontalStrut(10));
        filaCodigo.add(codigo);

        // Nombre
        JPanel filaNombre = new JPanel();
        filaNombre.setLayout(new BoxLayout(filaNombre, BoxLayout.X_AXIS));

        JLabel labelNombre = new JLabel("Producto: ");
        JTextField nombre = new JTextField();
        nombre.setMaximumSize(fieldSize);

        filaNombre.add(labelNombre);
        filaNombre.add(Box.createHorizontalStrut(10));
        filaNombre.add(nombre);

        // Salario
        JPanel filaSalario = new JPanel();
        filaSalario.setLayout(new BoxLayout(filaSalario, BoxLayout.X_AXIS));

        JLabel labelSalario = new JLabel("Provedor: ");
        JTextField salario = new JTextField();
        salario.setMaximumSize(fieldSize);

        filaSalario.add(labelSalario);
        filaSalario.add(Box.createHorizontalStrut(10));
        filaSalario.add(salario);

        // Fecha
        JPanel filaFecha = new JPanel();
        filaFecha.setLayout(new BoxLayout(filaFecha, BoxLayout.X_AXIS));

        JLabel labelFecha = new JLabel("Fecha factura: ");
        JDateChooser selectorFecha = new JDateChooser();
        selectorFecha.setMaximumSize(fieldSize);
        selectorFecha.setDateFormatString("dd/MM/yyyy");

        filaFecha.add(labelFecha);
        filaFecha.add(Box.createHorizontalStrut(10));
        filaFecha.add(selectorFecha);

        JPanel filaGuardar = new JPanel();
        JButton Guardado = new JButton("Guardar");
        filaGuardar.add(Guardado, BorderLayout.CENTER);
        // Agregar al panel principal
        panel.add(filaCodigo);
        panel.add(Box.createVerticalStrut(15));
        panel.add(filaNombre);
        panel.add(Box.createVerticalStrut(15));
        panel.add(filaSalario);
        panel.add(Box.createVerticalStrut(15));
        panel.add(filaFecha);
        panel.add(Box.createVerticalStrut(15));
        panel.add(filaGuardar);
    }
}
