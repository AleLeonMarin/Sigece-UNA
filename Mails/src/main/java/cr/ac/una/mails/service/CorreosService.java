package cr.ac.una.mails.service;

import cr.ac.una.mails.model.MailsDto;
import cr.ac.una.mails.model.UsersDto;
import cr.ac.una.mails.util.Request;
import cr.ac.una.mails.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CorreosService {

    private static final Logger LOG = Logger.getLogger(CorreosService.class.getName());


    public Respuesta guardarCorreo(MailsDto correoDto) {
        try {
            Request request = new Request("mails/save");
            request.post(correoDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            MailsDto correoGuardado = (MailsDto) request.readEntity(MailsDto.class);
            return new Respuesta(true, "", "", "Correo", correoGuardado);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error guardando el correo.", ex);
            return new Respuesta(false, "Error guardando el correo.", "guardarCorreo " + ex.getMessage());
        }
    }

    public Respuesta getCorreo(Long id) {
        try {
            Request request = new Request("mails/get/" + id);
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            MailsDto correo = (MailsDto) request.readEntity(MailsDto.class);
            return new Respuesta(true, "", "", "Correo", correo);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo el correo.", ex);
            return new Respuesta(false, "Error obteniendo el correo.", "getCorreo " + ex.getMessage());
        }
    }


    public Respuesta enviarCorreosPendientes() {
        try {
            Request request = new Request("mails/sendWaited");
            request.post(null);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<MailsDto> correosEnviados = (List<MailsDto>) request.readEntity(new GenericType<List<MailsDto>>() {});
            return new Respuesta(true, "", "", "Correos", correosEnviados);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error enviando correos pendientes.", ex);
            return new Respuesta(false, "Error enviando correos pendientes.", "enviarCorreosPendientes " + ex.getMessage());
        }
    }

    public Respuesta obtenerTodosLosCorreos() {
        try {
            Request request = new Request("mails/getAll");
            request.get();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<MailsDto> correos = (List<MailsDto>) request.readEntity(new GenericType<List<MailsDto>>() {});
            return new Respuesta(true, "", "", "Correos", correos);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error obteniendo todos los correos.", ex);
            return new Respuesta(false, "Error obteniendo todos los correos.", "obtenerTodosLosCorreos " + ex.getMessage());
        }
    }

    public Respuesta eliminarCorreo(Long id) {
        try {
            Request request = new Request("mails/delete/" + id);
            request.delete();

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "Correo eliminado correctamente.", "");

        } catch (Exception ex) {
            Logger.getLogger(CorreosService.class.getName()).log(Level.SEVERE, "Error eliminando el correo.", ex);
            return new Respuesta(false, "Error eliminando el correo.", "eliminarCorreo " + ex.getMessage());
        }
    }


    public Respuesta enviarCorreoAhora(MailsDto correoDto) {
        try {
            Request request = new Request("mails/sendNow");
            request.post(correoDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            Logger.getLogger(CorreosService.class.getName()).log(Level.SEVERE, "Error enviando el correo inmediatamente.", ex);
            return new Respuesta(false, "Error enviando el correo inmediatamente.", "enviarCorreoAhora " + ex.getMessage());
        }
    }

    public Respuesta enviarCorreoActivacion(MailsDto usuarioDto) {
        try {
            Request request = new Request("mails/activation");
            request.post(usuarioDto);

            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "", "Correo de activación enviado correctamente.");

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error enviando el correo de activación.", ex);
            return new Respuesta(false, "Error enviando el correo de activación.", "enviarCorreoActivacion " + ex.getMessage());
        }
    }

}
