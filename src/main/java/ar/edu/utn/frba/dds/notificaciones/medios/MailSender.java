package ar.edu.utn.frba.dds.notificaciones.medios;

import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;

public class MailSender implements MedioDeComunicacion {
  @Override
  public void notificarAperturaDeIncidente(Incidente incidente) {
    // TODO: implementar MailSender con javamail (sería nuestro adapter con javamail)
  }

  @Override
  public void sugerirRevisionDeIncidente(Incidente incidente) {

  }
}
