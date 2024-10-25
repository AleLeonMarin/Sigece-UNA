package cr.ac.una.wssigeceuna.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cr.ac.una.wssigeceuna.model.Mails;
import cr.ac.una.wssigeceuna.model.MailsDto;
import cr.ac.una.wssigeceuna.model.Notifications;
import cr.ac.una.wssigeceuna.model.ParamethersDto;
import cr.ac.una.wssigeceuna.model.UsersDto;
import cr.ac.una.wssigeceuna.util.CodigoRespuesta;
import cr.ac.una.wssigeceuna.util.Respuesta;

import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintViolationException;

@LocalBean
@Stateless
public class MailsService {

    private static final Logger LOG = Logger.getLogger(MailsService.class.getName());

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    @EJB
    ParamethersService paramethersService;

    @Inject
    EmailsService emailsService;

    public Respuesta saveMails(MailsDto mailsDto) {
        try {
            Mails mails;

            if (mailsDto.getId() != null && mailsDto.getId() > 0) {
                mails = em.find(Mails.class, mailsDto.getId());
                if (mails == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el correo a modificar.", "saveMails NoResultException");
                }
                mails.update(mailsDto);
                mails = em.merge(mails);
            } else {
                mails = new Mails(mailsDto);
                if (mailsDto.getNotification() != null && mailsDto.getNotification().getId() != null) {
                    Notifications notification = em.find(Notifications.class, mailsDto.getNotification().getId());
                    if (notification != null) {
                        mails.setNotification(notification);
                    }
                }
                em.persist(mails);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Correo", new MailsDto(mails));
        } catch (ConstraintViolationException ex) {
            ex.getConstraintViolations().forEach(violation -> LOG
                    .severe("Error de validación: " + violation.getPropertyPath() + " " + violation.getMessage()));
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Error al guardar el correo. Detalles: " + ex.getMessage(), "saveMails " + ex.getMessage());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el correo.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al guardar el correo.",
                    "saveMails " + ex.getMessage());
        }
    }

    public Respuesta sendMail(MailsDto mail) {
        try {
            String result = emailsService.waitingMails(mail.getDestinatary(), mail.getSubject(), mail.getResult());

            mail.setResult(result);
            mail.setDate(new Date());
            return saveMails(mail);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al enviar el correo.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al enviar el correo.",
                    "sendMail " + ex.getMessage());
        }
    }

    public Respuesta getMail(Long id) {
        try {
            Mails mails = em.find(Mails.class, id);
            if (mails == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el correo.",
                        "getMail NoResultException");
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Correo", new MailsDto(mails));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar el correo.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al consultar el correo.",
                    "getMail " + ex.getMessage());
        }
    }

    public Respuesta getMails() {
        try {
            Query query = em.createNamedQuery("Mails.findAll", Mails.class);
            List<Mails> mails = query.getResultList();
            List<MailsDto> mailsDto = new ArrayList<>();
            mails.forEach((mail) -> {
                mailsDto.add(new MailsDto(mail));
            });
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Correos", mailsDto);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al consultar los correos.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al consultar los correos.",
                    "getMails " + ex.getMessage());
        }
    }

    public Respuesta persistMails(MailsDto mailsDto) {
        try {
            Mails mails = new Mails(mailsDto);
            if (mailsDto.getId() != null && mailsDto.getId() > 0) {
                mails = em.merge(mails);
            } else {
                em.persist(mails);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Correo", new MailsDto(mails));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al guardar el correo.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al guardar el correo.",
                    "persistMails " + ex.getMessage());
        }
    }

    @Schedule(hour = "*", minute = "*", second = "30", persistent = false)
    public void sendPendientMail() {
        try {
            Respuesta paramethers = paramethersService.getParamethers();
            if (!paramethers.getEstado()) {
                LOG.log(Level.SEVERE, "Error obteniendo parámetros de configuración.");
                return;
            }

            ParamethersDto paramether = (ParamethersDto) paramethers.getResultado("Parametro");
            int mailsPerHour = (int) paramether.getTimeout();

            if (mailsPerHour <= 0) {
                LOG.log(Level.SEVERE, "El valor de correos por hora no es válido.");
                return;
            }

            int between = (60 / mailsPerHour) * 60 * 1000;
            LOG.log(Level.INFO, "Se enviará 1 correo cada {0} minutos.", (60 / mailsPerHour));

            Mails mailNotSend = em.createQuery("SELECT c FROM Mails c WHERE c.state = 'P'", Mails.class)
                    .setMaxResults(1)
                    .getSingleResult();

            if (mailNotSend != null) {
                String html = mailNotSend.getResult();
                String destinatary = mailNotSend.getDestinatary();
                String subject = mailNotSend.getSubject();

                String resultMail = emailsService.sendMail(destinatary, subject, html);

                if (resultMail.contains("exitosamente")) {
                    mailNotSend.setState("E");
                    em.merge(mailNotSend);
                    em.flush();

                    Thread.sleep(between - 30000);
                }
            }

        } catch (NoResultException ex) {
            LOG.log(Level.INFO, "No hay correos pendientes por enviar.");
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
            LOG.log(Level.SEVERE, "La tarea fue interrumpida.", ex);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error enviando el correo pendiente.", ex);
        }

    }

    public Respuesta sendMailNow(MailsDto mailsDto) {
        try {
            Mails mail;

            // Verificar si el correo ya existe en la base de datos
            if (mailsDto.getId() != null && mailsDto.getId() > 0) {
                mail = em.find(Mails.class, mailsDto.getId());
                if (mail == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                            "No se encontró el correo a enviar.", "sendMailNow");
                }
            } else {
                // Si es un correo nuevo, lo creamos
                mail = new Mails(mailsDto);
                em.persist(mail); // Persistir el nuevo correo
                em.flush();
            }

            // Enviar el correo
            String result = emailsService.sendMail(mail.getDestinatary(), mail.getSubject(), mail.getResult());

            // Verificar si el correo fue enviado correctamente
            if (result.contains("exitosamente")) {
                mail.setState("E"); // Cambiar el estado a 'Enviado'
                em.merge(mail); // Guardar los cambios en la base de datos
                em.flush();

                return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "Correo", new MailsDto(mail));
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al enviar el correo.",
                        "sendMailNow");
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error enviando el correo de inmediato.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error enviando el correo de inmediato.",
                    "sendMailNow");
        }
    }

    public Respuesta activationMail(UsersDto user) {
        try {

            Notifications notification = em.find(Notifications.class, 2L);
            if (notification == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO,
                        "No se encontró la notificación de activación.", "activationMail NoResultException");
            }

            String content = notification.getHtml()
                    .replace("[user]", user.getUser());

            // Crear el correo a persistir
            MailsDto mail = new MailsDto();
            mail.setSubject("Activación de cuenta SigeceUna");
            mail.setDestinatary(user.getEmail());
            mail.setResult(content);
            mail.setNotification(notification);
            mail.setState("E");
            mail.setDate(new Date());
            mail.setVersion(Long.MIN_VALUE);

            Respuesta saveMail = saveMails(mail);
            if (!saveMail.getEstado()) {
                return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error al guardar el correo de activación.",
                        "enviarCorreoActivacion " + saveMail.getMensaje());
            }

            // Enviar el correo utilizando el servicio de email
            String result = emailsService.sendMail(mail.getDestinatary(), mail.getSubject(),
                    content);
            if (result.contains("exitosamente")) {
                return new Respuesta(true, CodigoRespuesta.CORRECTO, "Correo de activación enviado exitosamente.", "",
                        "Correo", mail);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                        "Error enviando el correo de activación: " + result, "activationMail");
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Ocurrió un error al enviar el correo de activación.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO,
                    "Ocurrió un error al enviar el correo de activación.", "activationMail " + ex.getMessage());
        }
    }

    public Respuesta deleteMail(Long id) {
        try {
            Mails mail = em.find(Mails.class, id);
            if (mail == null) {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Correo no encontrado.",
                        "deleteMail");
            }

            em.remove(mail);
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "Correo eliminado correctamente.", "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error eliminando el correo.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Error eliminando el correo.",
                    "deleteMail " + ex.getMessage());
        }
    }

}
