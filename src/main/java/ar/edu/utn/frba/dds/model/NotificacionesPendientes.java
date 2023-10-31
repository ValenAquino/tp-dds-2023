package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioNotificaciones;
import ar.edu.utn.frba.dds.model.notificaciones.Notificacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class NotificacionesPendientes implements WithSimplePersistenceUnit {

  public static void main(String[] args) {
    new NotificacionesPendientes().notificarPendientes();
  }

  public void notificarPendientes() {
    withTransaction(() -> new RepositorioNotificaciones()
        .notificacionesPendientes()
        .forEach(Notificacion::enviar));
  }
}
