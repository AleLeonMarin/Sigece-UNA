package cr.ac.una.wssigeceuna.controller;

import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.MailsDto;
import cr.ac.una.wssigeceuna.model.UsersDto;
import cr.ac.una.wssigeceuna.service.MailsService;
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
import java.util.logging.Level;

@Secure
@Path("/mails")
@Tag(name = "Mails", description = "API para los correos del sistema")
public class MailsController {

    private static final Logger LOG = Logger.getLogger(MailsController.class.getName());

    @EJB
    MailsService mailsService;

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Guardar correos", description = "Guarda los correos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Correos guardados correctamente"),
        @ApiResponse(responseCode = "400", description = "Error al guardar los correos")
    })
  public Response saveMails(MailsDto mailsDto) {
    try {
        // Print para ver el contenido de mailsDto
        System.out.println("Debug - MailsDto recibido:");
        System.out.println("ID: " + mailsDto.getId());
        System.out.println("Asunto: " + mailsDto.getSubject());
        System.out.println("Destinatario: " + mailsDto.getDestinatary());
        System.out.println("Resultado: " + mailsDto.getResult());
        System.out.println("Estado: " + mailsDto.getState());
        System.out.println("Fecha: " + mailsDto.getDate());
        System.out.println("Versión: " + mailsDto.getVersion());

        // Verificar si la notificación está presente en mailsDto
        if (mailsDto.getNotification() != null) {
            System.out.println("Debug - Notificación en MailsDto:");
            System.out.println("Notificación ID: " + mailsDto.getNotification().getId());
            System.out.println("Notificación Nombre: " + mailsDto.getNotification().getName());
            System.out.println("Notificación HTML: " + mailsDto.getNotification().getHtml());
            System.out.println("Notificación Versión: " + mailsDto.getNotification().getVersion());
        } else {
            System.out.println("Debug - MailsDto no contiene una notificación asignada.");
        }

        Respuesta res = mailsService.saveMails(mailsDto);
        if (!res.getEstado()) {
            return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
        }
        return Response.ok(res).build();
    } catch (Exception ex) {
        LOG.severe("Ocurrió un error al guardar los correos.");
        return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                .entity("Ocurrió un error al guardar los correos.")
                .build();
    }
}


    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener correo", description = "Obtiene un correo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Correo encontrado"),
        @ApiResponse(responseCode = "404", description = "Correo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Error al obtener el correo")
    })
    public Response getMails(@PathParam("id") Long id) {
        try {
            Respuesta res = mailsService.getMail(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            MailsDto mailsDto = (MailsDto) res.getResultado("Correo");
            return Response.ok(mailsDto).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al obtener el correo.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener el correo.")
                    .build();
        }
    }

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Obtener correos", description = "Obtiene los correos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Correos encontrados", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = MailsDto.class))),
        @ApiResponse(responseCode = "404", description = "Correos no encontrados", content = @Content(mediaType = MediaType.TEXT_PLAIN)),
        @ApiResponse(responseCode = "500", description = "Error interno durante la obtención de los correos", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    })
    public Response getMails() {
        try {
            Respuesta res = mailsService.getMails();
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            return Response.ok(res.getResultado("Correos")).build();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al obtener los correos.", ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al obtener los correos.").build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Eliminar correo", description = "Elimina un correo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Correo eliminado"),
        @ApiResponse(responseCode = "404", description = "Correo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Error al eliminar el correo")
    })
    public Response deleteMails(@PathParam("id") Long id) {
        try {
            Respuesta res = mailsService.deleteMail(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok().build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al eliminar el correo.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al eliminar el correo.")
                    .build();
        }
    }

    @POST
    @Path("/sendWaited")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Enviar correos en espera", description = "Envía los correos en espera")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Correos enviados correctamente"),
        @ApiResponse(responseCode = "400", description = "Error al enviar los correos")
    })
    public Response sendWaitedMails() {
        try {

        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al enviar los correos en espera.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al enviar los correos en espera.")
                    .build();
        }
        return null;
    }

    @POST
    @Path("/sendNow")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Enviar correo", description = "Envía un correo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Correo enviado correctamente"),
        @ApiResponse(responseCode = "400", description = "Error al enviar el correo")
    })
    public Response sendEmail(MailsDto mailsDto) {
        try {
            Respuesta res = mailsService.sendMailNow(mailsDto);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al enviar el correo.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al enviar el correo.")
                    .build();
        }
    }

    @POST
    @Path("/activation")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Activar correo", description = "Activa un correo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Correo activado correctamente"),
        @ApiResponse(responseCode = "400", description = "Error al activar el correo")
    })
    public Response activationEmail(UsersDto user) {
        try {
            Respuesta res = mailsService.activationMail(user);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(res).build();
        } catch (Exception ex) {
            LOG.severe("Ocurrió un error al activar el correo.");
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue())
                    .entity("Ocurrió un error al activar el correo.")
                    .build();
        }
    }

}

