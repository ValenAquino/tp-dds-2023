package ar.edu.utn.frba.dds.model.notificaciones.medios;

import ar.edu.utn.frba.dds.model.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.model.notificaciones.Notificacion;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("WhatsApp")
public class WhatsAppSender extends MedioDeComunicacion {
  @Override
  public void procesarNotificacion(Notificacion notificacion) {
  }
}
