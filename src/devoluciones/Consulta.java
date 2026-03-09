package devoluciones;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.table.DefaultTableModel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Consulta extends JPanel {

    private JTable tabla;
    File archivo = new File("devoluciones.xlsx");
    JTextField txtProveedor = new JTextField(15);
    
    
    public Consulta(JPanel panel) {
        setLayout(new BorderLayout());
        tabla = new JTable();
        cargarExcelEnTabla();
        
        JButton btnDevolver = new JButton("Devolver producto");

        JPanel superior = new JPanel();
        superior.add(new JLabel("Proveedor:"));
        superior.add(txtProveedor);
        superior.add(btnDevolver);

        panel.add(superior, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(tabla);
        panel.add(scroll, BorderLayout.CENTER);


        btnDevolver.addActionListener(e -> devolverProductos());
         txtProveedor.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) { buscarProveedor(txtProveedor.getText()); }
            public void removeUpdate(DocumentEvent e) { buscarProveedor(txtProveedor.getText()); }
            public void changedUpdate(DocumentEvent e) { buscarProveedor(txtProveedor.getText()); }

        });
    }

    public void cargarExcelEnTabla() {
        
        try {
            Workbook workbook = new XSSFWorkbook(new FileInputStream(archivo));
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            DefaultTableModel model = new DefaultTableModel();

            Row headerRow = sheet.getRow(0);
            for (Cell cell : headerRow) {
                model.addColumn(formatter.formatCellValue(cell));
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Object[] fila = new Object[headerRow.getLastCellNum()];
                for (int j = 0; j < fila.length; j++) {
                    Cell cell = row.getCell(j);
                    fila[j] = (cell == null) ? "" : formatter.formatCellValue(cell);
                }
                model.addRow(fila);
            }
            
            tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            tabla.setModel(model);
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   public void buscarProveedor(String proveedor) {

        try {
            Workbook workbook = new XSSFWorkbook(new FileInputStream(archivo));
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            DefaultTableModel model = new DefaultTableModel();

            Row header = sheet.getRow(0);

            for (Cell cell : header) {
                model.addColumn(formatter.formatCellValue(cell));
            }

            proveedor = proveedor.toLowerCase().trim();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                String proveedorExcel = formatter.formatCellValue(row.getCell(2)).toLowerCase();

                if (proveedorExcel.contains(proveedor)) {

                    Object[] fila = new Object[header.getLastCellNum()];

                    for (int j = 0; j < fila.length; j++) {
                        fila[j] = formatter.formatCellValue(row.getCell(j));
                    }

                    model.addRow(fila);
                }
            }

            tabla.setModel(model);
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
public void devolverProductos() {

    int[] filas = tabla.getSelectedRows();

    if (filas.length == 0) {
        JOptionPane.showMessageDialog(this, "Seleccione productos");
        return;
    }

    try {

        FileInputStream fis = new FileInputStream("devoluciones.xlsx");
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        // recorrer las filas seleccionadas desde atrás
        for (int f = filas.length - 1; f >= 0; f--) {

            String idTabla = tabla.getValueAt(filas[f], 0).toString();

            for (int i = sheet.getLastRowNum(); i >= 1; i--) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                String idExcel = formatter.formatCellValue(row.getCell(0));

                if (idExcel.equals(idTabla)) {

                    sheet.removeRow(row);

                    if (i < sheet.getLastRowNum()) {
                        sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                    }

                    break;
                }
            }
        }

        fis.close();

        FileOutputStream fos = new FileOutputStream("devoluciones.xlsx");
        workbook.write(fos);

        fos.close();
        workbook.close();

        JOptionPane.showMessageDialog(this, "Productos devueltos");

        cargarExcelEnTabla();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
  
}