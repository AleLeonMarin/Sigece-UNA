package cr.ac.una.wssigeceuna.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.ApprovalsDto;
import cr.ac.una.wssigeceuna.service.ApprovalsService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/approvals")
public class ApprovalsController {

    @EJB
    ApprovalsService approvalsService;

    @POST
    @Path("/createApprovals")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createApprovals(ApprovalsDto approvals) {
        try {
            Respuesta res = approvalsService.createApprovals(approvals);
            if (!res.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok((ApprovalsDto) res.getResultado("approval")).build();
        } catch (Exception ex) {
            Logger.getLogger(ApprovalsController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al guardar la aprobación.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al guardar la aprobación.").build();
        }
    }

    @GET
    @Path("/getApprovals")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApprovals() {
        try {
            Respuesta res = approvalsService.getApprovals();
            if (!res.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(res.getResultado("approvals")).build();
        } catch (Exception ex) {
            Logger.getLogger(ApprovalsController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al obtener las aprobaciones.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener las aprobaciones.").build();
        }
    }

    @GET
    @Path("/getApproval/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApproval(Long id) {
        try {
            Respuesta res = approvalsService.getApproval(id);
            if (!res.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(res.getResultado("approval")).build();
        } catch (Exception ex) {
            Logger.getLogger(ApprovalsController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al obtener la aprobación.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener la aprobación.").build();
        }
    }

}
