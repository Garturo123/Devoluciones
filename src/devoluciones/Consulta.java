package devoluciones;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.table.DefaultTableModel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Consulta extends JPanel {

    private JTable tabla;
    File archivo = new File("devoluciones.xlsx");
    JTextField txtProveedor = new JTextField(15);
    Set<String> checksMarcados = new HashSet<>();
    
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

    // guardar checks actuales antes de refrescar
    checksMarcados.clear();

    if(tabla.getModel().getRowCount() > 0){
        for(int i = 0; i < tabla.getRowCount(); i++){

            Boolean check = (Boolean) tabla.getValueAt(i,0);

            if(check != null && check){
                String id = tabla.getValueAt(i,2).toString();
                checksMarcados.add(id);
            }
        }
    }

    try {
        Workbook workbook = new XSSFWorkbook(new FileInputStream(archivo));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        DefaultTableModel model = new DefaultTableModel(){

            @Override
            public Class<?> getColumnClass(int column) {
                if(column == 0) return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        model.addColumn("Devolver");
        model.addColumn("FilaExcel");

        Row headerRow = sheet.getRow(0);

        for (Cell cell : headerRow) {
            model.addColumn(formatter.formatCellValue(cell));
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;

            Object[] fila = new Object[headerRow.getLastCellNum() + 2];

            String id = formatter.formatCellValue(row.getCell(0));

            fila[0] = checksMarcados.contains(id); // restaurar check
            fila[1] = i;

            for(int j = 0; j < headerRow.getLastCellNum(); j++){
                Cell cell = row.getCell(j);
                fila[j+2] = formatter.formatCellValue(cell);
            }

            model.addRow(fila);
        }

        tabla.setModel(model);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);  // checkbox
        tabla.getColumnModel().getColumn(2).setPreferredWidth(80);  // ID
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150); // provedor
        tabla.getColumnModel().getColumn(4).setPreferredWidth(250); // producto
        tabla.getColumnModel().getColumn(5).setPreferredWidth(50); // cant
        tabla.getColumnModel().getColumn(6).setPreferredWidth(200);
        tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        workbook.close();

        // ocultar columna filaExcel completamente
        tabla.removeColumn(tabla.getColumnModel().getColumn(1));
        tabla.setAutoCreateRowSorter(true);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void buscarProveedor(String proveedor) {

    try {
        Workbook workbook = new XSSFWorkbook(new FileInputStream(archivo));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();

        DefaultTableModel model = new DefaultTableModel(){

            @Override
            public Class<?> getColumnClass(int column) {
                if(column == 0) return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        model.addColumn("Devolver");
        model.addColumn("FilaExcel");

        Row header = sheet.getRow(0);

        for (Cell cell : header) {
            model.addColumn(formatter.formatCellValue(cell));
        }

        proveedor = proveedor.toLowerCase().trim();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;

            String proveedorExcel = formatter.formatCellValue(row.getCell(1)).toLowerCase();

            if (proveedorExcel.contains(proveedor)) {

                Object[] fila = new Object[header.getLastCellNum() + 2];

                fila[0] = false;
                fila[1] = i;

                for(int j = 0; j < header.getLastCellNum(); j++){
                    Cell cell = row.getCell(j);
                    fila[j+2] = formatter.formatCellValue(cell);
                }

                model.addRow(fila);
            }
        }

        tabla.setModel(model);
        
       
        workbook.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

    tabla.getColumnModel().getColumn(1).setMinWidth(0);
    tabla.getColumnModel().getColumn(1).setMaxWidth(0);
    tabla.getColumnModel().getColumn(1).setWidth(0);
}
public void devolverProductos() {

    try {

        FileInputStream fis = new FileInputStream(archivo);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        ArrayList<Integer> filasExcel = new ArrayList<>();

        for(int i = 0; i < tabla.getRowCount(); i++){

            Boolean check = (Boolean) tabla.getValueAt(i,0);

            if(check != null && check){

                int filaExcel = (int) tabla.getModel().getValueAt(i,1);
                filasExcel.add(filaExcel);
            }
        }

        // eliminar desde atrás
        Collections.sort(filasExcel, Collections.reverseOrder());

        for(int fila : filasExcel){

            Row row = sheet.getRow(fila);

            if(row != null){
                sheet.removeRow(row);
            }

            if(fila < sheet.getLastRowNum()){
                sheet.shiftRows(fila+1, sheet.getLastRowNum(), -1);
            }
        }

        fis.close();

        FileOutputStream fos = new FileOutputStream(archivo);
        workbook.write(fos);

        fos.close();
        workbook.close();

        cargarExcelEnTabla();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}