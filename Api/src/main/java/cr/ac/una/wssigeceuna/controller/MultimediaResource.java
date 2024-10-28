package cr.ac.una.wssigeceuna.controller;

import cr.ac.una.wssigeceuna.model.VariablesDto;
import cr.ac.una.wssigeceuna.service.MultimediaService;
import cr.ac.una.wssigeceuna.service.VariablesService;
import cr.ac.una.wssigeceuna.util.Respuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Path("/multimedia")
@Tag(name = "Multimedia", description = "API para gestionar recursos multimedia")
public class MultimediaResource {

    @EJB
    private MultimediaService multimediaService;

    @EJB
    private VariablesService variablesService;

    @GET
    @Path("/imagen/{variableId}")
    @Produces({"image/png", "image/jpeg", "video/mp4"})
    @Operation(summary = "Obtener multimedia", description = "Obtiene una imagen o video multimedia desde la base de datos por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Multimedia encontrada y devuelta correctamente"),
        @ApiResponse(responseCode = "404", description = "Multimedia no encontrada en la base de datos"),
        @ApiResponse(responseCode = "500", description = "Error interno al intentar obtener la multimedia")
    })
    public Response obtenerImagen(@PathParam("variableId") Long variableId) {
        try {
            // Obtener la variable para verificar el tipo de contenido
            Respuesta res = variablesService.getVarible(variableId);
            if (!res.getEstado()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Multimedia no encontrada").build();
            }

            VariablesDto variableDto = (VariablesDto) res.getResultado("Variable");

            // Verificar si el contenido es imagen o video
            String fileType = variableDto.getValue();
            byte[] multimediaBytes = multimediaService.obtenerImagenDesdeDB(variableId);

            if (multimediaBytes == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Multimedia no encontrada").build();
            }

            InputStream multimediaStream = new ByteArrayInputStream(multimediaBytes);

            if (fileType != null && fileType.contains(".mp4")) {
                // Es un video
                return Response.ok(multimediaStream)
                        .header("Content-Disposition", "inline; filename=\"video_" + variableId + ".mp4\"")
                        .type("video/mp4")
                        .build();
            } else {
                // Es una imagen (asumimos PNG por defecto)
                return Response.ok(multimediaStream)
                        .header("Content-Disposition", "inline; filename=\"imagen_" + variableId + ".png\"")
                        .type("image/png")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al cargar el multimedia").build();
        }
    }
}