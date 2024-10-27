package cr.ac.una.wssigeceuna.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.RolesDto;
import cr.ac.una.wssigeceuna.model.SystemsDto;
import cr.ac.una.wssigeceuna.model.UsersDto;
import cr.ac.una.wssigeceuna.service.SystemsService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import io.swagger.v3.oas.annotations.Operation;
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

@Path("/SystemsController")
@Tag(name = "SystemsController", description = "Operaciones sobre los sistemas")
public class SystemsController {

    @EJB
    SystemsService systemsService;

    @GET
    @Path("/getSystems")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los sistemas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sistemas encontrados", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UsersDto.class))),
            @ApiResponse(responseCode = "404", description = "Sistemas no encontrados", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
            @ApiResponse(responseCode = "500", description = "Error interno durante la obtención del sistemas", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getSystems() {
        try {
            Respuesta res = systemsService.getSystems();
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<SystemsDto>>((List<SystemsDto>) res.getResultado("Sistemas")) {
            }).build();
        } catch (Exception ex) {
            Logger.getLogger(SystemsController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al obtener los sistemas.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener los sistemas.").build();
        }
    }

    @DELETE
    @Path("/deleteSystem/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sistema eliminado"),
            @ApiResponse(responseCode = "404", description = "Sistema no eliminado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
            @ApiResponse(responseCode = "500", description = "Error interno durante la eliminación del sistema", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response deleteSystem(@PathParam("id") Long id) {
        try {
            Respuesta res = systemsService.deleteSystem(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(SystemsController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al eliminar el sistema.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al eliminar el sistema.").build();
        }
    }

    @POST
    @Path("/saveSystem")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda un sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sistema guardado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SystemsDto.class))),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
            @ApiResponse(responseCode = "500", description = "Error interno durante el guardado del sistema", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response saveSystem(SystemsDto systemsDto) {
        try {
            Respuesta res = systemsService.saveSystem(systemsDto);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            SystemsDto systemDto = (SystemsDto) res.getResultado("Sistema");
            return Response.ok(systemDto).build();
        } catch (Exception ex) {
            Logger.getLogger(SystemsController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al guardar el sistema.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al guardar el sistema.").build();
        }
    }

    @GET
    @Path("/getSystem/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene un sistema por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sistema encontrado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SystemsDto.class))),
            @ApiResponse(responseCode = "404", description = "Sistema no encontrado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
            @ApiResponse(responseCode = "500", description = "Error interno durante la obtención del sistema", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })

    public Response getSystem(@PathParam("id") Long id) {
        try {
            Respuesta res = systemsService.getSystem(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            SystemsDto systemDto = (SystemsDto) res.getResultado("Sistema");
            System.out.println("Sistema ID: " + systemDto.getId());
            System.out.println("Roles en el sistema: " + systemDto.getRoles().size());
            for (RolesDto role : systemDto.getRoles()) {
                System.out.println("Rol ID: " + role.getId() + ", Nombre: " + role.getName());
            }
            return Response.ok(systemDto).build();
        } catch (Exception ex) {
            Logger.getLogger(SystemsController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al obtener el sistema.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener el sistema.").build();
        }
    }

}
