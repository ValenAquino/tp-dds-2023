package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioNotificaciones;
import ar.edu.utn.frba.dds.notificaciones.Notificacion;

public class NotificacionesPendientes {

  public static void main(String[] args) {
    RepositorioNotificaciones repo = new RepositorioNotificaciones();
    repo.notificacionesPendientes().forEach(Notificacion::enviar);
  }
}
