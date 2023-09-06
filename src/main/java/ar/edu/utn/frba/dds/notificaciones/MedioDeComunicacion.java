package ar.edu.utn.frba.dds.notificaciones;

import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Usuario;

public abstract class MedioDeComunicacion {

  public final void notificar(Notificacion notificacion) {
    procesarNotificacion(notificacion);
    notificacion.marcarComoEnviada();
  }

  public abstract void procesarNotificacion(Notificacion notificacion);
}
