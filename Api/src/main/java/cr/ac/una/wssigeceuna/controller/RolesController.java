package cr.ac.una.wssigeceuna.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.RolesDto;
import cr.ac.una.wssigeceuna.service.RolesService;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/RolesController")
@Tag(name = "RolesController", description = "Operaciones sobre los roles")
public class RolesController {

    @EJB
    RolesService rolesService;

    @POST
    @Path("/saveRol")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Guarda un rol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol guardado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = RolesDto.class))),
            @ApiResponse(responseCode = "404", description = "Rol no guardado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
            @ApiResponse(responseCode = "500", description = "Error interno durante la obtención del rol", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response saveRol(RolesDto rolesDto) {
        try {
            Respuesta res = rolesService.saveRol(rolesDto);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            RolesDto rolDto = (RolesDto) res.getResultado("Rol");
            return Response.ok(rolDto).build();
        } catch (Exception ex) {
            Logger.getLogger(RolesController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al guardar el rol.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al guardar el rol.").build();
        }
    }

    @GET
    @Path("/getRoles")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Obtiene todos los roles")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Roles encontrados", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = RolesDto.class))),
            @ApiResponse(responseCode = "404", description = "Roles no encontrados", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
            @ApiResponse(responseCode = "500", description = "Error interno durante la obtención de los roles", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getRoles() {
        try {
            Respuesta res = rolesService.getRoles();
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            return Response.ok(res.getResultado("Roles")).build();
        } catch (Exception ex) {
            Logger.getLogger(RolesController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al consultar los roles.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al consultar los roles.").build();
        }
    }

    @DELETE
    @Path("/deleteRol/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Elimina un rol")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol eliminado", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = RolesDto.class))),
            @ApiResponse(responseCode = "404", description = "Rol no eliminado", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
            @ApiResponse(responseCode = "500", description = "Error interno durante la eliminación del rol", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response deleteRol(@PathParam("id") Long id) {
        try {
            Respuesta res = rolesService.deleteRol(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            return Response.ok().build();
        } catch (Exception ex) {
            Logger.getLogger(RolesController.class.getName()).log(Level.SEVERE,
                    "Ocurrió un error al eliminar el rol.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al eliminar el rol.").build();
        }
    }

    
}
