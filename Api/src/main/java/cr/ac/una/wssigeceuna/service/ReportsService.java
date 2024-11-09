package cr.ac.una.wssigeceuna.service;

import cr.ac.una.wssigeceuna.model.Gestions;
import cr.ac.una.wssigeceuna.model.GestionsDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import cr.ac.una.wssigeceuna.util.ReportsUtil;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Stateless
public class ReportsService {

    private static final Logger LOG = Logger.getLogger(ReportsService.class.getName());

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta generateGestionesReport() {
        try {
            // Consultar las gestiones desde la base de datos
            TypedQuery<Gestions> query = em.createNamedQuery("Gestions.findAll", Gestions.class);
            List<Gestions> gestiones = query.getResultList();

            // Convertir las entidades a DTOs
            List<GestionsDto> gestionesDto = gestiones.stream()
                    .map(GestionsDto::new)
                    .collect(Collectors.toList());

            // Parámetros adicionales para el reporte
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("gestionesData", new JRBeanCollectionDataSource(gestionesDto));

            // Generar el reporte utilizando ReportsUtil
            byte[] reportPdf = ReportsUtil.generatePdfReport("reportGestiones.jrxml", gestionesDto, parameters);

            // Devolver el reporte como un arreglo de bytes
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "Reporte generado correctamente", "", "ReportePDF", reportPdf);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al generar el reporte de gestiones", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al generar el reporte de gestiones", ex.getMessage());
        }
    }
}
