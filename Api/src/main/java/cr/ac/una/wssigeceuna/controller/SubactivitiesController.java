package cr.ac.una.wssigeceuna.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.SubactivitiesDto;
import cr.ac.una.wssigeceuna.service.SubactivitiesService;
import cr.ac.una.wssigeceuna.util.Respuesta;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/SubactivitiesController")
@Tag(name = "SubactivitiesController", description = "Operaciones sobre las subactividades")
public class SubactivitiesController {

    @EJB
    SubactivitiesService subactivitiesService;

    @POST
    @Path("/saveSubactivity")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveSubactivity(SubactivitiesDto subactivity) {
        try {
            cr.ac.una.wssigeceuna.util.Respuesta res = subactivitiesService.saveSubactivity(subactivity);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            SubactivitiesDto subactivityDto = (SubactivitiesDto) res
                    .getResultado("Subactividad");
            return Response.ok(subactivityDto).build();
        } catch (Exception ex) {
            Logger.getLogger(SubactivitiesController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al guardar la subactividad.", ex);
            return Response.status(cr.ac.una.wssigeceuna.util.CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al guardar la subactividad.").build();
        }
    }

    @DELETE
    @Path("/deleteSubactivity/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSubactivity(@PathParam("id") Long id) {
        try {
            Respuesta res = subactivitiesService.deleteSubactivity(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(SubactivitiesController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al eliminar la subactividad.", ex);
            return Response.status(cr.ac.una.wssigeceuna.util.CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al eliminar la subactividad.").build();
        }
    }

}
