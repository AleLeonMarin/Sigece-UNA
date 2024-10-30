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
import java.util.zip.ZipInputStream;
import org.apache.commons.lang3.tuple.Pair;

@Stateless
@LocalBean
public class EmailsService {

    @PersistenceContext(unitName = "SigeceUnaWsPU")
    private EntityManager em;

    private static final Logger LOG = Logger.getLogger(EmailsService.class.getName());

    private Paramethers obtainParamethers() {
        return em.find(Paramethers.class, 1L);
    }

    public String sendMail(String destinary, String subject, String text, List<byte[]> attachments) {
        try {
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

            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(sender));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(destinary));
            mail.setSubject(subject);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            if (attachments != null) {
                for (byte[] fileData : attachments) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();

                    // Detectar tipo MIME
                    String mimeType = detectMimeType(fileData);
                    ByteArrayDataSource source = new ByteArrayDataSource(fileData, mimeType);

                    attachmentPart.setDataHandler(new DataHandler(source));
                    attachmentPart.setFileName("adjunto_" + attachments.indexOf(fileData)); // Nombre de archivo temporal
                    multipart.addBodyPart(attachmentPart);
                }
            }

            mail.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(sender, password);
            transport.sendMessage(mail, mail.getAllRecipients());
            transport.close();

            return "Correo enviado exitosamente con adjuntos.";
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

    public String detectMimeType(byte[] fileData) {
        try (InputStream is = new ByteArrayInputStream(fileData)) {
            // Intentar detección de tipo MIME a través de URLConnection
            String mimeType = URLConnection.guessContentTypeFromStream(is);

            if (mimeType == null) {
                // Intentar detectar archivos de Microsoft Office basados en encabezado ZIP
                try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(fileData))) {
                    if (zis.getNextEntry() != null) {
                        if (containsOfficeHeader(fileData, "[Content_Types].xml")) {
                            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document"; // DOCX
                        } else if (containsOfficeHeader(fileData, "ppt/")) {
                            return "application/vnd.openxmlformats-officedocument.presentationml.presentation"; // PPTX
                        } else if (containsOfficeHeader(fileData, "xl/")) {
                            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; // XLSX
                        }
                    }
                }

                // Verificar encabezados para tipos de archivos adicionales
                if (fileData.length > 4 && fileData[0] == 0x25 && fileData[1] == 0x50 && fileData[2] == 0x44 && fileData[3] == 0x46) {
                    mimeType = "application/pdf"; // PDF
                } else if (fileData.length > 3 && fileData[0] == (byte) 0xFF && fileData[1] == (byte) 0xD8 && fileData[2] == (byte) 0xFF) {
                    mimeType = "image/jpeg"; // JPEG
                } else if (fileData.length > 8 && fileData[0] == (byte) 0x89 && fileData[1] == (byte) 0x50 && fileData[2] == (byte) 0x4E && fileData[3] == (byte) 0x47) {
                    mimeType = "image/png"; // PNG
                } else if (fileData.length > 6 && fileData[0] == (byte) 0x47 && fileData[1] == (byte) 0x49 && fileData[2] == (byte) 0x46) {
                    mimeType = "image/gif"; // GIF
                } else {
                    mimeType = "application/octet-stream"; // Tipo MIME genérico
                }
            }
            return mimeType;
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

// Método auxiliar para verificar el encabezado en archivos de Office
    private boolean containsOfficeHeader(byte[] fileData, String header) {
        String content = new String(fileData);
        return content.contains(header);
    }

}
