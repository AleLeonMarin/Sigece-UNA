package cr.ac.una.wssigeceuna.controller;

import cr.ac.una.wssigeceuna.service.ReportsService;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reports")
public class ReportsController {

    @EJB
    private ReportsService reportsService;

    @GET
    @Path("/gestionesReport")
    @Produces("application/pdf")
    public Response generateGestionesReport() {
        Respuesta respuesta = reportsService.generateGestionesReport();
        if (!respuesta.getEstado()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta.getMensaje()).build();
        }

        byte[] pdfReport = (byte[]) respuesta.getResultado("ReportePDF");
        return Response.ok(pdfReport)
                .header("Content-Disposition", "attachment; filename=ReporteGestiones.pdf")
                .build();
    }
}