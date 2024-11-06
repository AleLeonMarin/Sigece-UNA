package cr.ac.una.wssigeceuna.controller;

import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.VariablesDto;
import cr.ac.una.wssigeceuna.service.VariablesService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import cr.ac.una.wssigeceuna.util.Secure;
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
import java.util.List;


@Path("/variables")
@Tag(name = "Variables", description = "API para las variables del sistema")
public class VariablesController {

    private static final Logger LOG = Logger.getLogger(VariablesController.class.getName());

    @EJB
    VariablesService variablesService;

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener variable", description = "Obtiene una variable por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Variable encontrada", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = VariablesDto.class))),
        @ApiResponse(responseCode = "404", description = "Variable no encontrada", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno durante la obtención de la variable", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getVariables(@PathParam("id") Long id) {
        try {
            Respuesta res = variablesService.getVarible(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            VariablesDto variableDto = (VariablesDto) res.getResultado("Variable");
            return Response.ok(variableDto).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al obtener la variable.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener la variable.").build();
        }
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Guardar variable", description = "Guarda una variable")
    public Response saveVariables(VariablesDto variablesDto) {
        try {
            Respuesta res = variablesService.saveVariable(variablesDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al guardar la variable.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al guardar la variable.").build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Eliminar variable", description = "Elimina una variable")
    public Response deleteVariables(@PathParam("id") Long id) {
        try {
            Respuesta res = variablesService.deleteVariable(id);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al eliminar la variable.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al eliminar la variable.").build();
        }
    }

    @GET
    @Path("/getVariables/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener variables por notificación", description = "Obtiene todas las variables de una notificación específica por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Variables encontradas", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = VariablesDto.class))),
        @ApiResponse(responseCode = "404", description = "Variables no encontradas", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno durante la obtención de las variables", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getVariablesNotification(@PathParam("id") Long id) {
        try {
            Respuesta res = variablesService.getByNotification(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            List<VariablesDto> variablesDto = (List<VariablesDto>) res.getResultado("Variables");
            return Response.ok(variablesDto).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al obtener las variables.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener las variables.").build();
        }
    }


}