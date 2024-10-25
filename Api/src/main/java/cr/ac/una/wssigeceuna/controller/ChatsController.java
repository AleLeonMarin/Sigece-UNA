package cr.ac.una.wssigeceuna.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.ChatsDto;
import cr.ac.una.wssigeceuna.service.ChatsService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;

@Path("/ChatsController")
@Tag(name = "Chats", description = "Operaciones sobre Chats")
public class ChatsController {

    private static final Logger LOG = Logger.getLogger(ChatsController.class.getName());

    @EJB
    ChatsService chatsService;

    @GET
    @Path("/chat/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un chat por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chat encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ChatsDto.class))),
            @ApiResponse(responseCode = "404", description = "Chat no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
            @ApiResponse(responseCode = "500", description = "Error interno durante la consulta", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getChat(@Parameter(description = "ID del Chat", required = true) @PathParam("id") Long id) {
        try {
            Respuesta res = chatsService.getChat(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok((ChatsDto) res.getResultado("Chat")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo el chat", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error obteniendo el chat").build();
        }
    }

    @GET
    @Path("/chats")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los chats")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chats obtenidos", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ChatsDto.class))),
            @ApiResponse(responseCode = "500", description = "Error interno durante la consulta", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getChats() {
        try {
            Respuesta res = chatsService.getChats();
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<ChatsDto>>((List<ChatsDto>) res.getResultado("Chats")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo los chats", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error obteniendo los chats")
                    .build();
        }
    }

    @POST
    @Path("/chat")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Agrega o actualiza un chat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chat agregado o actualizado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ChatsDto.class))),
            @ApiResponse(responseCode = "500", description = "Error al guardar el chat", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response guardarChat(
            @Parameter(description = "Datos del chat a agregar o actualizar", required = true) ChatsDto chatDto) {
        try {
            Respuesta respuesta = chatsService.saveChat(chatDto);
            if (respuesta.getEstado()) {
                return Response.ok((ChatsDto) respuesta.getResultado("Chat")).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(respuesta.getMensaje())
                        .build();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error guardando el chat", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error guardando el chat: " + ex.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/chat/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un chat por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chat eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Chat no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
            @ApiResponse(responseCode = "500", description = "Error interno durante la eliminación", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response eliminarChat(@PathParam("id") Long id) {
        try {
            Respuesta respuesta = chatsService.deleteChat(id);

            if (respuesta.getEstado()) {
                return Response.ok().build();
            } else {
                return Response.status(respuesta.getCodigoRespuesta().getValue())
                        .entity(respuesta.getMensaje())
                        .build();
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error eliminando el chat", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error eliminando el chat: " + ex.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/chats/usuario/{usuarioId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatsByUsuario(@PathParam("usuarioId") Long usuarioId) {
        try {
            Respuesta res = chatsService.getChatsByUsuario(usuarioId);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<ChatsDto>>((List<ChatsDto>) res.getResultado("Chats")) {
            }).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo los chats del usuario", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Error obteniendo los chats del usuario").build();
        }
    }

    @GET
    @Path("/chats/{idEmisor}/{idReceptor}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene los chats entre dos usuarios (emisor y receptor)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chats entre los usuarios obtenidos", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ChatsDto.class))),
            @ApiResponse(responseCode = "500", description = "Error al obtener los chats entre usuarios", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getChatsEntreUsuarios(@PathParam("idEmisor") Long idEmisor,
            @PathParam("idReceptor") Long idReceptor) {
        try {
            Respuesta respuesta = chatsService.getChatsEntreUsuarios(idEmisor, idReceptor);
            if (respuesta.getEstado()) {
                List<ChatsDto> chats = (List<ChatsDto>) respuesta.getResultado("Chats");
                return Response.ok(new GenericEntity<List<ChatsDto>>(chats) {
                }).build();
            } else {
                return Response.status(respuesta.getCodigoRespuesta().getValue()).entity(respuesta.getMensaje())
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener los chats entre usuarios: " + e.getMessage())
                    .build();
        }
    }
}
