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
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipInputStream;
import org.apache.tika.Tika;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tika.mime.MimeTypes;

@Stateless
@LocalBean
public class EmailsService {
    
    private Tika tika = new Tika();

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    private static final Logger LOG = Logger.getLogger(EmailsService.class.getName());

    private Paramethers obtainParamethers() {
        return em.find(Paramethers.class, 1L);
    }

    public String sendMail(String destinary, String subject, String text, List<byte[]> attachments, List<String> contentIds) {
        try {
            // Configuración SMTP
            Paramethers parametros = obtainParamethers();
            Properties props = new Properties();
            props.put("mail.smtp.host", parametros.getServer());
            props.put("mail.smtp.port", parametros.getPort());
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            String sender = parametros.getMail();
            String password = parametros.getPassword();

            Session session = Session.getInstance(props);
            session.setDebug(true);

            // Crear mensaje
            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(sender));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(destinary));
            mail.setSubject(subject);

            // Parte del cuerpo HTML
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

             if (attachments != null) {
                for (int i = 0; i < attachments.size(); i++) {
                    byte[] fileData = attachments.get(i);

                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    String mimeType = detectMimeType(fileData);
                    ByteArrayDataSource source = new ByteArrayDataSource(fileData, mimeType);

                    attachmentPart.setDataHandler(new DataHandler(source));

                    if (contentIds != null && i < contentIds.size() && contentIds.get(i) != null) {
                        String contentId = contentIds.get(i);
                        attachmentPart.setHeader("Content-ID", "<" + contentId + ">");
                        attachmentPart.setDisposition(MimeBodyPart.INLINE); 
                    } else {
                        attachmentPart.setDisposition(MimeBodyPart.ATTACHMENT);
                        String fileName = "attachment_" + (i + 1) + obtenerExtensionConTika(fileData);
                        attachmentPart.setFileName(fileName);
                    }

                    multipart.addBodyPart(attachmentPart);
                }
            }

            mail.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(sender, password);
            transport.sendMessage(mail, mail.getAllRecipients());
            transport.close();

            return "Correo enviado exitosamente con adjuntos inline.";
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
        return "Todos los correos fueron procesados. Resultados:\n"
                + mails.stream()
                        .map(c -> "Correo a: " + c.getDestinatary() + " - Estado: " + c.getState())
                        .collect(Collectors.joining("\n"));
    }

    public String waitingMails(String destinary, String subject, String text) {
        try {

            Paramethers paramethers = obtainParamethers();

            long wait = paramethers.getTimeout();

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

  private String detectMimeType(byte[] fileData) {
        try (InputStream is = new ByteArrayInputStream(fileData)) {
            String mimeType = tika.detect(is);
            return mimeType != null ? mimeType : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

   
  
    private String obtenerExtensionConTika(byte[] fileContent) {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("tempfile", null);
            Files.write(tempFile, fileContent);

            String mimeType = tika.detect(tempFile);
            System.out.println("Detected MIME type: " + mimeType);
            return MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension();
        } catch (Exception e) {
            return ".bin";
        } finally {
            if (tempFile != null) {
                try {
                    Files.delete(tempFile);
                } catch (IOException ignored) {
                }
            }
        }
    }



}
