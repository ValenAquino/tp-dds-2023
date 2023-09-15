package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.entidades.Comunidad;
import ar.edu.utn.frba.dds.notificaciones.Notificacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioNotificaciones implements WithSimplePersistenceUnit {
  private static RepositorioNotificaciones instance;

  public static RepositorioNotificaciones getInstance() {
    if (instance == null) {
      instance = new RepositorioNotificaciones();
    }
    return instance;
  }

  public void persistir(Notificacion notificacion) {
    entityManager().persist(notificacion);
  }

  @SuppressWarnings("unchecked")
  public List<Notificacion> todas() {
    return entityManager().createQuery("from notificaciones").getResultList();
  }
  public List<Notificacion> notificacionesPendientes() {
    return todas().stream()
        .filter(n -> !n.fueEnviada())
        .collect(Collectors.toList());

    // TODO: Implementar con query --> Borrar todas()
  }
}