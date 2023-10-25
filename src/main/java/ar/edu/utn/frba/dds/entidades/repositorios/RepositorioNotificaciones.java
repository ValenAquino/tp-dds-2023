package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.notificaciones.Notificacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
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
    return entityManager().createQuery("SELECT n FROM Notificacion", Notificacion.class).getResultList();
  }
  public List<Notificacion> notificacionesPendientes() {
    return todas().stream()
        .filter(n -> !n.fueEnviada())
        .collect(Collectors.toList());
  }
  public List<Notificacion> notificacionesDelUsuario(Usuario usuario) {
    return  entityManager().createQuery("SELECT n FROM Notificacion n WHERE n.receptor = :usuario", Notificacion.class)
        .setParameter("usuario", usuario)
        .getResultList();
  }
}