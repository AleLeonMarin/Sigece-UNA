package cr.ac.una.admin.service;

import java.util.HashMap;
import java.util.Map;
import cr.ac.una.admin.util.Request;
import cr.ac.una.admin.util.Respuesta;

public class ReportsService {

    public Respuesta generateGestionesReport(Long employeeId, String creationStartDate, String creationEndDate) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("employeeId", employeeId);
            params.put("creationStartDate", creationStartDate);
            params.put("creationEndDate", creationEndDate);

            Request request = new Request("reports/gestionesReport?employeeId="+employeeId+"&creationStartDate="+creationStartDate+"&creationEndDate="+creationEndDate);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            byte[] pdfReport = (byte[]) request.readEntity(byte[].class);
            return new Respuesta(true, "", "", "ReportePDF", pdfReport);
        } catch (Exception e) {
            return new Respuesta(false, "Error generando el reporte de gestiones.", e.getMessage());
        }
    }

    public Respuesta generateGestionesPerformanceReport(Long areaId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("areaId", areaId);

            Request request = new Request();

            if (areaId == null) {
                //Request request = new Request("reports/gestionesPerformanceReport");
                request = new Request("reports/gestionesPerformanceReport");
                request.get();
            }else{
            //Request request = new Request("reports/gestionesPerformanceReport?areaId=" + areaId);
            request = new Request("reports/gestionesPerformanceReport?areaId=" + areaId);
            request.get();
            }

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            byte[] pdfReport = (byte[]) request.readEntity(byte[].class);
            return new Respuesta(true, "", "", "ReportePDF", pdfReport);

        } catch (Exception e) {
            return new Respuesta(false, "Error generando el reporte de rendimiento de gestiones.", e.getMessage());
        }
    }

    public Respuesta generateGestionesAsignadasReport(Long assignedUserId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("assignedUserId", assignedUserId);

            Request request = new Request("reports/gestionesAsignadasReport?assignedUserId=" + assignedUserId);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            byte[] pdfReport = (byte[]) request.readEntity(byte[].class);
            return new Respuesta(true, "", "", "ReportePDF", pdfReport);
        } catch (Exception e) {
            return new Respuesta(false, "Error generando el reporte de gestiones asignadas.", e.getMessage());
        }
    }
}
