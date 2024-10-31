package cr.ac.una.wssigeceuna.controller;

import java.util.List;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.AreasDto;
import cr.ac.una.wssigeceuna.model.UsersDto;
import cr.ac.una.wssigeceuna.service.AreasService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/areas")
@Tag(name = "Areas", description = "API para las areas del sistema")
public class AreasController {

    private static final Logger LOG = Logger.getLogger(AreasController.class.getName());

    @EJB
    AreasService areasService;

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener area", description = "Obtiene una area")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Area encontrada"),
            @ApiResponse(responseCode = "400", description = "Error al obtener la area"),
            @ApiResponse(responseCode = "404", description = "Area no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno durante la obtención del area")
    })
    public Response getAreas(@PathParam("id") Long id) {
        try {
            Respuesta res = areasService.getArea(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok((AreasDto) res.getResultado("Area")).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al obtener la area.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener la area.").build();
        }
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Guardar area", description = "Guarda una area")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Area guardada"),
            @ApiResponse(responseCode = "400", description = "Error al guardar la area"),
            @ApiResponse(responseCode = "500", description = "Error interno durante la obtención del area")
    })
    public Response saveAreas(AreasDto areasDto) {
        try {
            Respuesta res = areasService.createArea(areasDto);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok((AreasDto) res.getResultado("Area")).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al guardar la area.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al guardar la area.").build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Eliminar area", description = "Elimina una area")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Area eliminada"),
            @ApiResponse(responseCode = "400", description = "Error al eliminar la area"),
            @ApiResponse(responseCode = "500", description = "Error interno durante la eliminación del area")
    })
    public Response deleteAreas(@PathParam("id") Long id) {
        try {
            Respuesta res = areasService.deleteArea(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al eliminar la area.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al eliminar la area.").build();
        }
    }

    @GET
    @Path("/getAreas")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener areas", description = "Obtiene todas las areas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Areas encontradas"),
            @ApiResponse(responseCode = "400", description = "Error al obtener las areas"),
            @ApiResponse(responseCode = "404", description = "Areas no encontradas"),
            @ApiResponse(responseCode = "500", description = "Error interno durante la obtención de las areas")
    })
    public Response getAreas() {
        try {
            Respuesta res = areasService.getAreas();
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(new GenericEntity<List<AreasDto>>((List<AreasDto>) res.getResultado("Areas")) {
            }).build();

        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al obtener las areas.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener las areas.").build();
        }
    }

}
