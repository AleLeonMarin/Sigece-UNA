package cr.ac.una.wssigeceuna.service;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import cr.ac.una.wssigeceuna.model.MailsDto;
import cr.ac.una.wssigeceuna.model.Paramethers;

@Stateless
@LocalBean
public class EmailsService {

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    private static final Logger LOG = Logger.getLogger(EmailsService.class.getName());

    private Paramethers obtainParamethers() {
        return em.find(Paramethers.class, 1L);
    }

    public String sendMail(String destinary, String subject, String text) {
        try {

            Paramethers parametros = obtainParamethers();

            Properties props = new Properties();
            props.put("mail.smtp.host", parametros.getServer());
            props.put("mail.smtp.port", parametros.getPort());
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            String sender = parametros.getMail(); // Correo remitente
            String password = parametros.getPassword(); // Contraseña

            // Crear la sesión de correo
            Session session = Session.getInstance(props);
            session.setDebug(true);

            // Crear el mensaje de correo
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(sender)); // Establecer remitente
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(destinary)); // Establecer destinatario
            mail.setSubject(subject); // Asunto del correo
            mail.setText(text, "UTF-8", "html"); // Cuerpo del correo en formato HTML

            // Enviar el correo
            Transport transport = session.getTransport("smtp");
            transport.connect(sender, password); // Autenticarse
            transport.sendMessage(mail, mail.getAllRecipients()); // Enviar mensaje
            transport.close(); // Cerrar la conexión

            return "Correo enviado exitosamente.";
        } catch (MessagingException e) {
            return "Error enviando el correo: " + e.getMessage();
        }
    }

    public String sendMails(List<MailsDto> mails) {
        Paramethers parametros = obtainParamethers();

        Properties props = new Properties();
        props.put("mail.smtp.host", parametros.getServer());
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        String sender = parametros.getMail();
        String password = parametros.getPassword();

        Session session = Session.getInstance(props);
        session.setDebug(true);

        for (MailsDto mailDto : mails) {
            try {
                MimeMessage mail = new MimeMessage(session);
                mail.setFrom(new InternetAddress(sender));
                mail.addRecipient(Message.RecipientType.TO, new InternetAddress(mailDto.getDestinatary()));
                mail.setSubject(mailDto.getSubject());
                mail.setText(mailDto.getResult(), "UTF-8", "html");

                Transport transport = session.getTransport("smtp");
                transport.connect(sender, password);
                transport.sendMessage(mail, mail.getAllRecipients());
                transport.close();

                mailDto.setState("E");
            } catch (MessagingException e) {
                LOG.severe("Error al enviar el correo a: " + mailDto.getDestinatary() + " - " + e.getMessage());
                mailDto.setState("P");
            }
        }
        return "Todos los correos fueron procesados. Resultados:\n" +
                mails.stream()
                        .map(c -> "Correo a: " + c.getDestinatary() + " - Estado: " + c.getState())
                        .collect(Collectors.joining("\n"));
    }

    public String waitingMails(String destinary, String subject, String text) {
        try {

            Paramethers paramethers = obtainParamethers();

            long wait = paramethers.getTimeout();

            // Esperar el tiempo configurado antes de enviar el correo
            Thread.sleep(wait);

            Properties props = new Properties();
            props.put("mail.smtp.host", paramethers.getServer());
            props.put("mail.smtp.port", paramethers.getPort());
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            String sender = paramethers.getMail();
            String password = paramethers.getPassword();

            Session session = Session.getInstance(props);
            session.setDebug(true);

            MimeMessage mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(sender));
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinary)); // Establecer destinatario
            mensaje.setSubject(subject); // Asunto del correo
            mensaje.setText(text, "UTF-8", "html");

            Transport transport = session.getTransport("smtp");
            transport.connect(sender, password);
            transport.sendMessage(mensaje, mensaje.getAllRecipients());
            transport.close();

            return "Correo enviado exitosamente después de " + wait + " ms.";
        } catch (MessagingException e) {
            return "Error enviando el correo: " + e.getMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
            return "Error: La espera fue interrumpida.";
        }
    }

}
