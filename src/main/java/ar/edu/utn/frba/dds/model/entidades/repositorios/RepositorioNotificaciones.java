package ar.edu.utn.frba.dds.model.entidades.repositorios;

import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.PersistentEntity;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.notificaciones.Notificacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import java.util.stream.Stream;

public class RepositorioNotificaciones implements WithSimplePersistenceUnit {
  private static RepositorioNotificaciones instance;

  public static RepositorioNotificaciones getInstance() {
    if (instance == null) {
      instance = new RepositorioNotificaciones();
    }
    return instance;
  }

  public List<Notificacion> deIncidentes(List<Incidente> incidentes) {
    return Stream.concat(
        entityManager()
            .createQuery("FROM NotificacionNuevoIncidente n WHERE n.incidente.id IN (:ids_incidentes)",
                Notificacion.class)
            .setParameter("ids_incidentes", incidentes.stream().map(PersistentEntity::getId).toList())
            .getResultList().stream(),
        entityManager()
            .createQuery("FROM NotificacionRevisionIncidente n WHERE n.incidente.id IN (:ids_incidentes)",
                Notificacion.class)
            .setParameter("ids_incidentes", incidentes.stream().map(PersistentEntity::getId).toList())
            .getResultList().stream()
    ).toList();
  }

  public void persistir(Notificacion notificacion) {
    entityManager().persist(notificacion);
  }

  public List<Notificacion> notificacionesPendientes() {
    return entityManager().createQuery("SELECT n FROM Notificacion n WHERE n.fechaEnvio IS NULL", Notificacion.class)
        .getResultList();
  }

  public List<Notificacion> notificacionesDelUsuario(Usuario usuario) {
    return entityManager().createQuery("SELECT n FROM Notificacion n WHERE n.receptor = :usuario", Notificacion.class)
        .setParameter("usuario", usuario)
        .getResultList();
  }

  public void eliminar(Notificacion notificacion) {
    entityManager().remove(notificacion);
  }
}