package devoluciones;

import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.io.*;
import java.util.HashSet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class Crear extends JPanel{
    
    public Crear(JPanel panel){
         
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        

        Dimension fieldSize = new Dimension(250, 30);

        // Codigo
        JPanel filaCodigo = new JPanel();
        filaCodigo.setLayout(new BoxLayout(filaCodigo, BoxLayout.X_AXIS));

        JLabel labelCodigo = new JLabel("No. Factura: ");
        JTextField codigo = new JTextField();
        codigo.setMaximumSize(fieldSize);

        JLabel labelProvedor = new JLabel("Provedor: ");
        JTextField provedor = new JTextField();
        provedor.setMaximumSize(fieldSize);

        filaCodigo.add(labelProvedor);
        filaCodigo.add(Box.createHorizontalStrut(10));
        filaCodigo.add(provedor);
        filaCodigo.add(Box.createHorizontalStrut(20));
        filaCodigo.add(labelCodigo);
        filaCodigo.add(Box.createHorizontalStrut(10));
        filaCodigo.add(codigo);

        // Nombre
        JPanel filaNombre = new JPanel();
        filaNombre.setLayout(new BoxLayout(filaNombre, BoxLayout.X_AXIS));

        JLabel labelNombre = new JLabel("Producto: ");
        JTextField nombre = new JTextField();
        nombre.setMaximumSize(fieldSize);
        JLabel labelCantidad = new JLabel("Cantidad: ");
        JSpinner cantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        cantidad.setMaximumSize(fieldSize);
        
        filaNombre.add(labelNombre);
        filaNombre.add(Box.createHorizontalStrut(10));
        filaNombre.add(nombre);
        filaNombre.add(Box.createHorizontalStrut(20));
        filaNombre.add(labelCantidad);
        filaNombre.add(Box.createHorizontalStrut(10));
        filaNombre.add(cantidad);
        
        JPanel filaMotivo = new JPanel();
        filaMotivo.setLayout(new BoxLayout(filaMotivo, BoxLayout.X_AXIS));
        
        JLabel labelMotivo = new JLabel("Motivo Devolucion: ");
        JTextField motivo = new JTextField();
        motivo.setMaximumSize(fieldSize);
        
        filaMotivo.add(labelMotivo);
        filaMotivo.add(Box.createHorizontalStrut(10));
        filaMotivo.add(motivo);
        // Fecha
        JPanel filaFecha = new JPanel();
        filaFecha.setLayout(new BoxLayout(filaFecha, BoxLayout.X_AXIS));

        JLabel labelFecha = new JLabel("Fecha factura: ");
        JDateChooser selectorFecha = new JDateChooser();
        selectorFecha.setMaximumSize(fieldSize);
        selectorFecha.setDateFormatString("dd/MM/yyyy");
        JButton Guardado = new JButton("Guardar");
        Guardado.setBackground(Color.green);
        
        filaFecha.add(labelFecha);
        filaFecha.add(Box.createHorizontalStrut(10));
        filaFecha.add(selectorFecha);
        filaFecha.add(Box.createHorizontalStrut(20));
        filaFecha.add(Guardado);
        // Agregar al panel principal
        panel.add(filaCodigo);
        panel.add(Box.createVerticalStrut(15));
        panel.add(filaNombre);
        panel.add(Box.createVerticalStrut(15));
        panel.add(filaMotivo);
        panel.add(Box.createVerticalStrut(15));
        panel.add(filaFecha);
        
        
        Guardado.addActionListener(e -> {
            String getNoFactura = codigo.getText();
            String getProvedor = provedor.getText();
            String getProducto= nombre.getText();
            String getMotivo= motivo.getText();
            String getCantidad = cantidad.getValue().toString();
            
            
            if(!getNoFactura.equals("") && !getProvedor.equals("")  && !getProducto.equals("") && !getMotivo.equals("")){
                try{
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaTexto = formato.format(selectorFecha.getDate());
                    AgregarRegistro(getNoFactura,getProvedor,getProducto,getCantidad,getMotivo, fechaTexto);
                    codigo.setText("");
                    provedor.setText("");
                    nombre.setText("");
                    motivo.setText("");
                    cantidad.setValue(1);
                    selectorFecha.setDate(null);
                }catch(Exception i){
                    System.out.println("Agregue una fecha valida");
                }
                

                
            }

        });
    }
    public void AgregarRegistro(String factura, String proveedor, String producto, String cantidad, String motivo, String fecha){

    try{

        File archivo = new File("devoluciones.xlsx");

        Workbook libro;
        Sheet hoja;
        System.out.println(new File(".").getAbsolutePath());
        if(!archivo.exists()){

            libro = new XSSFWorkbook();
            hoja = libro.createSheet("Devoluciones");
            
            System.out.println("Exisitosamente creado");
            Row encabezado = hoja.createRow(0);
            
            encabezado.createCell(0).setCellValue("ID");
            encabezado.createCell(1).setCellValue("Factura");
            encabezado.createCell(2).setCellValue("Proveedor");
            encabezado.createCell(3).setCellValue("Producto");
            encabezado.createCell(4).setCellValue("Cantidad");
            encabezado.createCell(5).setCellValue("Motivo");
            encabezado.createCell(6).setCellValue("Fecha");

        }else{

            FileInputStream fis = new FileInputStream(archivo);
            libro = new XSSFWorkbook(fis);
            hoja = libro.getSheetAt(0);
            fis.close();

        }
        int ultimaFila = hoja.getLastRowNum() + 1;

        // crear nueva fila
        Row fila = hoja.createRow(ultimaFila);
        int nuevoID = obtenerIDDisponible(hoja);

        
        fila.createCell(0).setCellValue(nuevoID);
        fila.createCell(1).setCellValue(factura);
        fila.createCell(2).setCellValue(proveedor);
        fila.createCell(3).setCellValue(producto);
        fila.createCell(4).setCellValue(cantidad);
        fila.createCell(5).setCellValue(motivo);
        fila.createCell(6).setCellValue(fecha);

        // guardar archivo
        FileOutputStream fos = new FileOutputStream(archivo);
        libro.write(fos);
        
        fos.close();
        libro.close();

        System.out.println("Fila agregada correctamente");
        }catch(Exception e){
        
        }

    }
    public int obtenerIDDisponible(Sheet sheet) {

        DataFormatter formatter = new DataFormatter();
        HashSet<Integer> ids = new HashSet<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;

            String valor = formatter.formatCellValue(row.getCell(0));

            if (!valor.isEmpty()) {
                ids.add(Integer.parseInt(valor));
            }
        }

        int id = 1;

        while (ids.contains(id)) {
            id++;
        }

        return id;
    }
}
