package cr.ac.una.wssigeceuna.service;

import cr.ac.una.wssigeceuna.model.Gestions;
import cr.ac.una.wssigeceuna.model.GestionsDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import cr.ac.una.wssigeceuna.util.ReportsUtil;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
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

    public Respuesta generateGestionesReport(Long employeeId, LocalDate creationStartDate, LocalDate creationEndDate) {
        try {
            // Consulta modificada para incluir el rango de fechas de creación
            TypedQuery<Gestions> query = em.createQuery(
                    "SELECT g FROM Gestions g WHERE "
                    + "((g.requester.id = :employeeId) OR (g.assigned.id = :employeeId)) AND "
                    + "(g.creationDate BETWEEN :creationStartDate AND :creationEndDate)", Gestions.class);
            query.setParameter("employeeId", employeeId);
            query.setParameter("creationStartDate", creationStartDate);
            query.setParameter("creationEndDate", creationEndDate);

            List<Gestions> gestiones = query.getResultList();

            // Convertir las entidades a DTOs
            List<GestionsDto> gestionesDto = gestiones.stream()
                    .map(GestionsDto::new)
                    .collect(Collectors.toList());

            // Parámetros adicionales para el reporte
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("gestionesData", new JRBeanCollectionDataSource(gestionesDto));
            parameters.put("creationStartDate", creationStartDate);
            parameters.put("creationEndDate", creationEndDate);

            // Generar el reporte utilizando ReportsUtil
            byte[] reportPdf = ReportsUtil.generatePdfReport("reportGestiones.jrxml", gestionesDto, parameters);

            // Devolver el reporte como un arreglo de bytes
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "Reporte generado correctamente", "", "ReportePDF", reportPdf);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al generar el reporte de gestiones", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al generar el reporte de gestiones", ex.getMessage());
        }
    }

    public Respuesta generateGestionesPerformanceReport(Long areaId) {
        try {
            // Consulta para obtener gestiones
            String queryStr = "SELECT g FROM Gestions g JOIN g.activity act JOIN act.area ar ";
            if (areaId != null) {
                queryStr += "WHERE ar.id = :areaId";
            }

            TypedQuery<Gestions> query = em.createQuery(queryStr, Gestions.class);
            if (areaId != null) {
                query.setParameter("areaId", areaId);
            }

            List<Gestions> gestiones = query.getResultList();
            List<GestionsDto> gestionesDto = gestiones.stream()
                    .map(g -> {
                        LocalDate a = LocalDate.of(2024, 11, 10);
                        GestionsDto dto = new GestionsDto(g);
                        dto.setOnTime(dto.getState().equals("C") && dto.getSolutionDate() != null && !dto.getSolutionDate().isBefore(LocalDate.now()));
                        return dto;
                    })
                    .collect(Collectors.toList());

            // Calcular los conteos para los gráficos
            long enTiempoCount = gestionesDto.stream().filter(dto -> dto.getOnTime()).count();
            long fueraDeTiempoCount = gestionesDto.stream().filter(dto -> !dto.getOnTime()).count();
            long pendientesCount = gestionesDto.stream().filter(g -> g.getState().equals("S")).count();
            long atendidasCount = gestionesDto.stream().filter(g -> g.getState().equals("A")).count();

            // Parámetros para el reporte
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("gestionesData", new JRBeanCollectionDataSource(gestionesDto));
            parameters.put("areaId", areaId);
            parameters.put("enTiempoCount", (int) enTiempoCount);
            parameters.put("fueraDeTiempoCount", (int) fueraDeTiempoCount);
            parameters.put("pendientesCount", (int) pendientesCount);
            parameters.put("atendidasCount", (int) atendidasCount);

            byte[] reportPdf = ReportsUtil.generatePdfReport("reportGestionesArea.jrxml", gestionesDto, parameters);

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "Reporte generado correctamente", "", "ReportePDF", reportPdf);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al generar el reporte de rendimiento de gestiones", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al generar el reporte de rendimiento de gestiones", ex.getMessage());
        }
    }

}
