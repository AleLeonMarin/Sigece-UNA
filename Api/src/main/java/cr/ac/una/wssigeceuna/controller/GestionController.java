package cr.ac.una.wssigeceuna.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.GestionsDto;
import cr.ac.una.wssigeceuna.service.GestionService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;

@Path("/gestion")
@Tag(name = "Gestion", description = "API para la gestion de las gestiones")
public class GestionController {

    @EJB
    private GestionService gestionService;

    @GET
    @Path("/getGestions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtiene las gestiones", description = "Obtiene todas las gestiones")
    public Response getGestions() {
        try {
            Respuesta respuesta = gestionService.getGestiones();
            if (!respuesta.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_NOENCONTRADO.getValue()).entity(respuesta.getMensaje())
                        .build();
            }
            return Response
                    .ok(new GenericEntity<List<GestionsDto>>((List<GestionsDto>) respuesta.getResultado("Gestiones")) {
                    }).build();
        } catch (Exception ex) {
            Logger.getLogger(GestionController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al consultar los usuarios.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al consultar los usuarios.").build();
        }

    }

    @GET
    @Path("/getGestion/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtiene una gestion", description = "Obtiene una gestion por su id")
    public Response getGestion(@PathParam("id") Long id) {
        try {
            Respuesta respuesta = gestionService.getGestion(id);
            if (!respuesta.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_NOENCONTRADO.getValue()).entity(respuesta.getMensaje())
                        .build();
            }
            return Response.ok((GestionsDto) respuesta.getResultado("Gestion")).build();
        } catch (Exception ex) {
            Logger.getLogger(GestionController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al consultar la gestion.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al consultar la gestion.").build();
        }

    }

    @POST
    @Path("/createGestion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Crea una gestion", description = "Crea una gestion")
    public Response createGestion(GestionsDto gestion) {
        try {
            Respuesta respuesta = gestionService.createGestion(gestion);
            if (!respuesta.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity(respuesta.getMensaje()).build();
            }
            return Response.ok((GestionsDto) respuesta.getResultado("Gestion")).build();
        } catch (Exception ex) {
            Logger.getLogger(GestionController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al guardar la gestion.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al guardar la gestion.").build();
        }

    }

    @DELETE
    @Path("/deleteGestion/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Elimina una gestion", description = "Elimina una gestion por su id")
    public Response deleteGestion(@PathParam("id") Long id) {
        try {
            Respuesta respuesta = gestionService.deleteGestion(id);
            if (!respuesta.getEstado()) {
                return Response.status(CodigoRespuesta.ERROR_NOENCONTRADO.getValue()).entity(respuesta.getMensaje())
                        .build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(GestionController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al eliminar la gestion.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al eliminar la gestion.").build();
        }

    }
}
