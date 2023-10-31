package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioNotificaciones;
import ar.edu.utn.frba.dds.model.notificaciones.Notificacion;
import ar.edu.utn.frba.dds.model.notificaciones.medios.MailSender;
import org.junit.jupiter.api.Disabled;

public class MailSenderTest {

  @Disabled
  public void enviarMail() {
    Usuario usuario = new Usuario(
        "marcos",
        "contraseña",
        "marcos",
        "marano",
        "marcosmarano3@gmail.com",
        RepositorioComunidades.getInstance(),
        RepositorioNotificaciones.getInstance()
    );

    Notificacion notificacion = new Notificacion(usuario) {
      @Override
      public String getAsunto() {
        return "Prueba Mail Sender";
      }

      @Override
      public String getMensaje() {
        return "Esta es una notificación para probar el Mail Sender";
      }
    };

    new MailSender().procesarNotificacion(notificacion);
  }
}
