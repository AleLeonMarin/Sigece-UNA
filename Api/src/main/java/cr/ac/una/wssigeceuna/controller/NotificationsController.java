package cr.ac.una.wssigeceuna.controller;

import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.NotificationsDto;
import cr.ac.una.wssigeceuna.service.NotificationService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.PermitAll;
import cr.ac.una.wssigeceuna.util.Respuesta;
import cr.ac.una.wssigeceuna.util.Secure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Secure
@Path("/notifications")
@Tag(name = "Notifications", description = "API para las notificaciones del sistema")
public class NotificationsController {

    private static final Logger LOG = Logger.getLogger(NotificationsController.class.getName());

    @EJB
    NotificationService notificationsService;

    
    @PermitAll
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener notificación", description = "Obtiene una notificación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
            @ApiResponse(responseCode = "400", description = "Error al obtener la notificación")
    })
    public Response getNotifications(@PathParam("id") Long id) {
        try {
            Respuesta res = notificationsService.getNotification(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok((NotificationsDto) res.getResultado("Notificacion")).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al obtener la notificación.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener la notificación.")
                    .build();
        }
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Guardar notificación", description = "Guarda una notificación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación guardada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al guardar la notificación")
    })
    public Response saveNotifications(NotificationsDto notificationsDto) {
        try {
            Respuesta res = notificationsService.saveNotification(notificationsDto);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al guardar la notificación.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al guardar la notificación.")
                    .build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación eliminada"),
            @ApiResponse(responseCode = "404", description = "Notificación no eliminada"),
            @ApiResponse(responseCode = "400", description = "Error al eliminar la notificación")
    })
    public Response deleteNotifications(@PathParam("id") Long id) {
        try {
            Respuesta res = notificationsService.deleteNotification(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al eliminar la notificación.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al eliminar la notificación.")
                    .build();
        }
    }

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener notificaciones", description = "Obtiene las notificaciones")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificaciones encontradas"),
            @ApiResponse(responseCode = "400", description = "Error al obtener las notificaciones")
    })
    public Response getNotifications() {
        try {
            Respuesta res = notificationsService.getAll();
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(res.getResultado("Notificaciones")).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al obtener las notificaciones.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener las notificaciones.")
                    .build();
        }
    }

}
