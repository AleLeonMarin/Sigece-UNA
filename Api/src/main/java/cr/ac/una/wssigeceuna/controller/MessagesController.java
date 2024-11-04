package cr.ac.una.wssigeceuna.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.MessagesDto;
import cr.ac.una.wssigeceuna.service.MessagesService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/messages")
@Tag(name = "Messages", description = "API to manage messages")
public class MessagesController {

    @EJB
    MessagesService messagesService;

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get message by id", description = "Get message by id")
    public Response getMessageById(@PathParam("id") Long id) {
        try {
            Respuesta res = messagesService.getMessage(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            Logger.getLogger(MessagesController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al obtener el mensaje.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("An error occurred while getting the message.")
                    .build();
        }
    }

    @POST
    @Path("/messages")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Save message", description = "Save message")
    public Response saveMessage(MessagesDto messagesDto) {
        try {
            Respuesta res = messagesService.saveMessage(messagesDto);

            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue())
                        .entity(res.getMensaje())
                        .build();
            }

            return Response.ok((MessagesDto) res.getResultado("Mensaje")).build();
        } catch (Exception ex) {
            Logger.getLogger(MessagesController.class.getName()).log(Level.SEVERE, "Error guardando el mensaje.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("An error occurred while saving the message.")
                    .build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete message", description = "Delete message")
    public Response deleteMessage(@PathParam("id") Long id) {
        try {
            Respuesta res = messagesService.deleteMessages(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue())
                        .entity(res.getMensaje())
                        .build();
            }

            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(MessagesController.class.getName()).log(Level.SEVERE, "Error eliminando el mensaje.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("An error occurred while deleting the message.")
                    .build();
        }
    }

    @GET
    @Path("/getByChat/{chatId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get messages by chat", description = "Get all messages associated with a specific chat")
    public Response getMensajesByChat(@PathParam("chatId") Long chatId) {
        try {
            Respuesta res = messagesService.getMensajesByChat(chatId);

            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue())
                        .entity(res.getMensaje())
                        .build();
            }

            List<MessagesDto> mensajes = (List<MessagesDto>) res.getResultado("Mensajes");
            return Response.ok(mensajes).build();
        } catch (Exception ex) {
            Logger.getLogger(MessagesController.class.getName()).log(Level.SEVERE,
                    "Error al obtener mensajes del chat.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("An error occurred while retrieving messages for the chat.")
                    .build();
        }
    }

    @GET
    @Path("/download/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Download attachment", description = "Download attachment for the given message ID")
    public Response descargarArchivoAdjunto(@PathParam("id") Long id) {
        try {
            Respuesta res = messagesService.getArchivoAdjunto(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            MessagesDto mensajeDto = (MessagesDto) res.getResultado("ArchivoAdjunto");
            return Response.ok(mensajeDto).build(); // Devolvemos el MessagesDto completo como JSON
        } catch (Exception ex) {
            Logger.getLogger(MessagesController.class.getName()).log(Level.SEVERE,
                    "Error al descargar el archivo adjunto.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al descargar el archivo adjunto.")
                    .build();
        }
    }

}
