package fr.pixteam.pixcms.mailing;

import fr.pixteam.pixcms.Application;
import fr.pixteam.pixcms.managers.Environment;
import fr.pixteam.pixcms.utils.Checks;

import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailManager {

    private final Session session;
    private final boolean mailingEnabled;

    public MailManager() {
        boolean mailingStatus;
        try {
            mailingStatus = !Environment.MAILING_ENABLED.get().trim().isEmpty() && Boolean.parseBoolean(Environment.MAILING_ENABLED.get());
        } catch (Exception e) {
            Application.getLogger().error("Cannot activate the mailing", e);
            mailingStatus = false;
        }
        mailingEnabled = mailingStatus;
        if (mailingEnabled) {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", Environment.SMTP_HOST.get());
            properties.put("mail.smtp.port", Environment.SMTP_PORT.get());
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

            session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(Environment.OUTGOING_EMAIL_ADDRESS.get(), Environment.SMTP_PASSWORD.get());
                }
            });
        } else {
            session = null;
        }
    }

    public void sendMail(String subject, String body, List<String> to) throws MessagingException {
        if (!mailingEnabled) {
            Application.getLogger().warn("Trying to send an email but mailing has been disabled!");
            return;
        }
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Environment.OUTGOING_EMAIL_ADDRESS.get()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Checks.notBlank("recipients", String.join(",", to))));
        message.setSubject(Checks.notBlank("subject", subject));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(Checks.notBlank("body", body), "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}
