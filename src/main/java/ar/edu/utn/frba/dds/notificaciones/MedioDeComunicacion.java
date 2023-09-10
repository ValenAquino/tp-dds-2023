package ar.edu.utn.frba.dds.notificaciones;

public abstract class MedioDeComunicacion {

  public final void notificar(Notificacion notificacion) {
    procesarNotificacion(notificacion);
    notificacion.marcarComoEnviada();
  }

  public abstract void procesarNotificacion(Notificacion notificacion);
}
