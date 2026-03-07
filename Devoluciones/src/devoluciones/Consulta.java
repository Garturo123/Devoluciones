package devoluciones;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.table.DefaultTableModel;
import java.io.FileInputStream;

public class Consulta extends JPanel {

    private JTable tabla;
    private JButton btnAbrir;

    public Consulta(JPanel panel) {
        setLayout(new BorderLayout());

        tabla = new JTable();
        btnAbrir = new JButton("Abrir Excel");

        btnAbrir.addActionListener(e -> abrirExcel());

        JScrollPane scroll = new JScrollPane(tabla);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(btnAbrir, BorderLayout.SOUTH);
    }

    public void cargarExcelEnTabla(String rutaExcel) {

        try {
            Workbook workbook = new XSSFWorkbook(new FileInputStream(rutaExcel));
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

            tabla.setModel(model);
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirExcel() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos Excel (*.xlsx)", "xlsx"));

        int resultado = chooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            cargarExcelEnTabla(archivo.getAbsolutePath());
        }
    }
}