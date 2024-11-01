package cr.ac.una.wssigeceuna.controller;

import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.ParamethersDto;
import cr.ac.una.wssigeceuna.service.ParamethersService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import jakarta.ws.rs.*;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.util.JwTokenHelper;
import cr.ac.una.wssigeceuna.util.Secure;



@Secure
@Path("/paramethers")
@Tag(name = "Paramethers", description = "API para los parámetros del sistema")
@SecurityRequirement(name = "jwt-auth")
public class ParamethersController {

    private static final Logger LOG = Logger.getLogger(ParamethersController.class.getName());

    @EJB
    ParamethersService paramethersService;

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Guardar parámetros del sistema", description = "Guarda los parámetros del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parámetros guardados correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al guardar los parámetros")
    })
    public Response saveParamethers(ParamethersDto paramethersDto) {
        try {
            Respuesta res = paramethersService.saveParamethers(paramethersDto);
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al guardar los parámetros.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al guardar los parámetros.")
                    .build();
        }
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener parámetros del sistema", description = "Obtiene los parámetros del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parámetros obtenidos correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al obtener los parámetros")
    })
    public Response getParamethers() {
        try {
            Respuesta res = paramethersService.getParamethers();
            ParamethersDto paramethersDto = (ParamethersDto) res.getResultado("Parametro");
            return Response.ok(paramethersDto).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al obtener los parámetros.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener los parámetros.")
                    .build();
        }
    }

}
