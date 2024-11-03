package cr.ac.una.wssigeceuna.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.ActivitiesDto;
import cr.ac.una.wssigeceuna.service.ActivitiesService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/ActivitiesController")
@Tag(name = "ActivitiesController", description = "Operaciones sobre las actividades")
public class ActivitiesController {

    @EJB
    ActivitiesService activitiesService;

    @POST
    @Path("/saveActivity")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveActivity(ActivitiesDto activity) {
        try {
            Respuesta res = activitiesService.saveActivity(activity);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            ActivitiesDto activityDto = (ActivitiesDto) res.getResultado("Actividad");
            return Response.ok(activityDto).build();
        } catch (Exception ex) {
            Logger.getLogger(ActivitiesController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al guardar la actividad.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al guardar la actividad.").build();
        }
    }

    @DELETE
    @Path("/deleteActivity/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteActivity(@PathParam("id") Long id) {
        try {
            Respuesta res = activitiesService.deleteActivity(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(ActivitiesController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al eliminar la actividad.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al eliminar la actividad.").build();
        }
    }

    @GET
    @Path("/getActivity/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivity(@PathParam("id") Long id) {
        try {
            Respuesta res = activitiesService.getActivity(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            ActivitiesDto activityDto = (ActivitiesDto) res.getResultado("Actividad");
            return Response.ok(activityDto).build();
        } catch (Exception ex) {
            Logger.getLogger(ActivitiesController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al obtener la actividad.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener la actividad.").build();
        }
    }

    @GET
    @Path("/getActivities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivities() {
        try {
            Respuesta res = activitiesService.getActivities();
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            return Response.ok(res.getResultado("Actividades")).build();
        } catch (Exception ex) {
            Logger.getLogger(ActivitiesController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al obtener las actividades.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener las actividades.").build();
        }
    }

}
