package cr.ac.una.wssigeceuna.controller;

import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.service.EmailsService;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/emails")
@Tag(name = "Emails", description = "API para los correos del sistema")
public class EmailsController {

    private static final Logger LOG = Logger.getLogger(EmailsController.class.getName());

    @EJB
    EmailsService emailsService;

    @POST
    @Path("/send")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(summary = "Enviar correo", description = "Envía un correo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Correo enviado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al enviar el correo")
    })
    public Response sendEmail(@FormParam("to") String to, @FormParam("subject") String subject,
            @FormParam("body") String body) {
        try {
            String result = emailsService.sendMail(to, subject, body);
            return Response.ok(result).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al enviar el correo.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al enviar el correo.")
                    .build();
        }
    }

    @POST
    @Path("/sendHtml")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(summary = "Enviar correo HTML", description = "Envía un correo en formato HTML")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Correo enviado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al enviar el correo")
    })
    public Response sendHtmlEmail(@FormParam("to") String to, @FormParam("subject") String subject,
            @FormParam("body") String body) {
        try {
            String result = emailsService.sendMail(to, subject, body);
            return Response.ok(result).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al enviar el correo.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al enviar el correo.")
                    .build();
        }
    }

    @POST
    @Path("/sendWait")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Enviar correo en espera", description = "Envía un correo en espera")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Correo enviado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al enviar el correo")
    })
    public Response sendWaitEmail(@QueryParam("to") String to, @QueryParam("subject") String subject,
            @QueryParam("body") String body) {
        try {
            String result = emailsService.waitingMails(to, subject, body);
            return Response.ok(result).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al enviar el correo.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al enviar el correo.")
                    .build();
        }
    }

}
