package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.notificaciones.Notificacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityTransaction;

public class RepositorioNotificaciones implements WithSimplePersistenceUnit {
  private static RepositorioNotificaciones instance;

  public static RepositorioNotificaciones getInstance() {
    if (instance == null) {
      instance = new RepositorioNotificaciones();
    }
    return instance;
  }

  public void persistir(Notificacion notificacion) {
    EntityTransaction transaction = entityManager().getTransaction();
    try {
      transaction.begin();
      entityManager().persist(notificacion);
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
    }
  }


  public List<Notificacion> todas() {
    return entityManager().createQuery("from Notificacion").getResultList();
  }
  public List<Notificacion> notificacionesPendientes() {
    return todas().stream()
        .filter(n -> !n.fueEnviada())
        .collect(Collectors.toList());

    // TODO: Implementar con query --> Borrar todas()
  }
}