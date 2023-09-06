package ar.edu.utn.frba.dds.notificaciones.medios;

import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.notificaciones.Notificacion;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender extends MedioDeComunicacion {
  private final Properties propiedades = new Properties();

  public MailSender() {
    // TODO: configurar
    propiedades.put("mail.smtp.auth", "true");
    propiedades.put("mail.smtp.starttls.enable", "true");
    propiedades.put("mail.smtp.host", "smtp.gmail.com");
    propiedades.put("mail.smtp.port", "587");
    propiedades.put("mail.username", "username");
    propiedades.put("mail.password", "password");
  }

  @Override
  public void procesarNotificacion(Notificacion notificacion) {
    enviarEmail(new Mail(
        notificacion.getAsunto(),
        notificacion.getMensaje(),
        notificacion.getReceptor().getCorreoElectronico()
    ));
  }

  private void enviarEmail(Mail mail) {
    try {
      Transport.send(crearMensaje(crearSesion(), mail));
    } catch (MessagingException e) {
      throw new RuntimeException("Error al enviar el correo electrónico.", e);
    }
  }

  private Session crearSesion() {
    return Session.getInstance(propiedades, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(
            propiedades.getProperty("mail.username"),
            propiedades.getProperty("mail.password")
        );
      }
    });
  }

  private Message crearMensaje(Session sesion, Mail mail) throws MessagingException {
    Message mensaje = new MimeMessage(sesion);
    mensaje.setFrom(new InternetAddress(propiedades.getProperty("mail.username")));
    mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getDestinatario()));
    mensaje.setSubject(mail.getAsunto());
    mensaje.setText(mail.getCuerpo());
    return mensaje;
  }
}
