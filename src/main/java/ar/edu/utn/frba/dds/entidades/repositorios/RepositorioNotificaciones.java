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
    entityManager().persist(notificacion);
  }
  public List<Notificacion> todas() {
    return entityManager().createQuery("SELECT n FROM Notificacion", Notificacion.class)
        .getResultList();
  }
  public List<Notificacion> notificacionesPendientes() {
    return entityManager().createQuery("SELECT n FROM Notificacion n WHERE n.fechaEnvio IS NULL", Notificacion.class)
        .getResultList();
  }
  public List<Notificacion> notificacionesDelUsuario(Usuario usuario) {
    return  entityManager().createQuery("SELECT n FROM Notificacion n WHERE n.receptor = :usuario", Notificacion.class)
        .setParameter("usuario", usuario)
        .getResultList();
  }
}