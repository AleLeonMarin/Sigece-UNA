package cr.ac.una.wssigeceuna.service;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Messages;
import cr.ac.una.wssigeceuna.model.MessagesDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;

import cr.ac.una.wssigeceuna.model.Chats;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypes;

@Stateless
@LocalBean
public class MessagesService {

    private static final Logger LOG = Logger.getLogger(MessagesService.class.getName());

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    public Respuesta getMessage(Long id) {
        try {
            Messages sms = em.find(Messages.class, id);
            if (sms == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró el mensaje con el ID proporcionado.", "getMessage NoResultException");
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Mensaje", new MessagesDto(sms));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el mensaje.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al consultar el mensaje.",
                    "getMessage " + ex.getMessage());
        }
    }

    public Respuesta getMensajesByChat(Long chatId) {
        try {
            Query qryMensajes = em.createNamedQuery("Messages.findByChat", Messages.class);
            qryMensajes.setParameter("chat", chatId);

            List<Messages> sms = qryMensajes.getResultList();
            List<MessagesDto> smsDto = new ArrayList<>();
            for (Messages message : sms) {
                smsDto.add(new MessagesDto(message));
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Mensajes", smsDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                    "No se encontraron mensajes para este chat.", "getMensajesByChat NoResultException");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar los mensajes del chat.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al consultar los mensajes del chat.", "getMensajesByChat " + ex.getMessage());
        }
    }

    public Respuesta saveMessage(MessagesDto sms) {
        try {
            if (sms.getChat() == null || sms.getChat().getId() == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "El mensaje debe pertenecer a un chat existente con un ID válido.", "saveMessage ChatIDNullException");
            }

            Chats chat = em.find(Chats.class, sms.getChat().getId());
            if (chat == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró el chat asociado al mensaje.", "saveMessage ChatNotFoundException");
            }

            Messages mensaje;
            if (sms.getId() != null && sms.getId() > 0) {
                mensaje = em.find(Messages.class, sms.getId());
                if (mensaje == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el mensaje a modificar.", "saveMessage MessageNotFoundException");
                }
                mensaje.update(sms);
                mensaje.setChat(chat);
                mensaje = em.merge(mensaje);
            } else {
                mensaje = new Messages(sms);
                mensaje.setChat(chat);
                em.persist(mensaje);
            }

            // Procesar y guardar archivo adjunto, si existe
            if (sms.getArchive() != null) {
                mensaje.setArchive(sms.getArchive());
            }

            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Mensaje", new MessagesDto(mensaje));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el mensaje.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al guardar el mensaje.",
                    "saveMessage " + ex.getMessage());
        }
    }

    public Respuesta sendMessages(List<MessagesDto> mensajesDtoList) {
        try {
            List<Messages> mensajes = new ArrayList<>();
            for (MessagesDto mensajeDto : mensajesDtoList) {
                Messages mensaje = new Messages(mensajeDto);
                em.persist(mensaje);
                mensajes.add(mensaje);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Mensajes", mensajesDtoList);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al enviar los mensajes.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al enviar los mensajes.",
                    "enviarMensajes " + ex.getMessage());
        }
    }

    public Respuesta deleteMessages(Long id) {
        try {
            Messages mensaje = em.find(Messages.class, id);
            if (mensaje == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el mensaje a eliminar.",
                        "eliminarMensaje NoResultException");
            }
            em.remove(mensaje);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al eliminar el mensaje.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrió un error al eliminar el mensaje.",
                    "eliminarMensaje " + ex.getMessage());
        }
    }

   
    public Respuesta getArchivoAdjunto(Long mensajeId) {
        try {
            Messages mensaje = em.find(Messages.class, mensajeId);
            if (mensaje == null || mensaje.getArchive() == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "El mensaje o archivo adjunto no fue encontrado.", "getArchivoAdjunto NoResultException");
            }

            Tika tika = new Tika();
            String tipoMime = tika.detect(mensaje.getArchive());
            String extension = "";
            try {
                extension = MimeTypes.getDefaultMimeTypes().forName(tipoMime).getExtension();
            } catch (Exception e) {
                extension = ".bin"; // Asignar una extensión predeterminada si no se puede detectar
            }

            MessagesDto mensajeDto = new MessagesDto(mensaje);
            mensajeDto.setMimeType(tipoMime);
            mensajeDto.setExtension(extension);

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "ArchivoAdjunto", mensajeDto);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al obtener el archivo adjunto.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al obtener el archivo adjunto.", "getArchivoAdjunto " + ex.getMessage());
        }
    }

}
