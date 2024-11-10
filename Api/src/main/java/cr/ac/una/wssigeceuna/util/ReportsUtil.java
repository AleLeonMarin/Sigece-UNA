package cr.ac.una.wssigeceuna.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.JRPdfExporter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.pdf.SimplePdfExporterConfiguration;
import net.sf.jasperreports.pdf.type.PdfVersionEnum;

public class ReportsUtil {

    public static byte[] generatePdfReport(String jrxmlPath, List<?> data, Map<String, Object> parameters) throws JRException, FileNotFoundException {
        // Cargar el archivo JRXML desde el classpath
        InputStream reportStream = ReportsUtil.class.getClassLoader().getResourceAsStream(jrxmlPath);
        if (reportStream == null) {
            throw new FileNotFoundException("El archivo " + jrxmlPath + " no se encontró en el classpath.");
        }

        // Compilar el reporte JRXML
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Llenar el reporte con datos
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Configuración para exportar el reporte a PDF usando OpenPDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRPdfExporter pdfExporter = new JRPdfExporter();
        pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        configuration.setPdfVersion(PdfVersionEnum.VERSION_1_7);
        pdfExporter.setConfiguration(configuration);

        // Exportar el reporte
        pdfExporter.exportReport();

        // Devolver el PDF generado como un arreglo de bytes
        return outputStream.toByteArray();
    }
}
