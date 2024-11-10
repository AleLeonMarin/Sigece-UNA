package cr.ac.una.wssigeceuna.controller;

import cr.ac.una.wssigeceuna.service.ReportsService;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Path("/reports")
public class ReportsController {

    @EJB
    private ReportsService reportsService;

    @GET
    @Path("/gestionesReport")
    @Produces("application/pdf")
    public Response generateGestionesReport(
            @QueryParam("employeeId") Long employeeId,
            @QueryParam("creationStartDate") String creationStartDateStr,
            @QueryParam("creationEndDate") String creationEndDateStr) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate creationStartDate = LocalDate.parse(creationStartDateStr, formatter);
        LocalDate creationEndDate = LocalDate.parse(creationEndDateStr, formatter);

        Respuesta respuesta = reportsService.generateGestionesReport(employeeId, creationStartDate, creationEndDate);
        if (!respuesta.getEstado()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta.getMensaje()).build();
        }

        byte[] pdfReport = (byte[]) respuesta.getResultado("ReportePDF");
        return Response.ok(pdfReport)
                .header("Content-Disposition", "attachment; filename=ReporteGestiones.pdf")
                .build();
    }

    @GET
    @Path("/gestionesPerformanceReport")
    @Produces("application/pdf")
    public Response generateGestionesPerformanceReport(
            @QueryParam("areaId") Long areaId) {

        Respuesta respuesta = reportsService.generateGestionesPerformanceReport(areaId);
        if (!respuesta.getEstado()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta.getMensaje()).build();
        }

        byte[] pdfReport = (byte[]) respuesta.getResultado("ReportePDF");
        return Response.ok(pdfReport)
                .header("Content-Disposition", "attachment; filename=ReporteRendimientoGestiones.pdf")
                .build();
    }
}
