package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.notificaciones.Notificacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityTransaction;

public class RepositorioIncidentes implements WithSimplePersistenceUnit {
  private static RepositorioIncidentes instance;

  public static RepositorioIncidentes getInstance() {
    if (instance == null) {
      instance = new RepositorioIncidentes();
    }
    return instance;
  }

  public void persistir(Incidente incidente) {
    EntityTransaction transaction = entityManager().getTransaction();
    try {
      transaction.begin();
      entityManager().persist(incidente);
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
    }
  }

  public List<Incidente> ultimaSemana() {
    LocalDateTime ahora = LocalDateTime.now();
    LocalDateTime fechaLimite = ahora.minusDays(7);

    return todos().stream()
        .filter(i -> i.getFecha().isAfter(fechaLimite) && i.getFecha().isBefore(ahora))
        .collect(Collectors.toList());
  }

  public List<Incidente> todos() {
    return entityManager().createQuery("SELECT i FROM Incidente", Incidente.class)
        .getResultList();
  }
  public List<Incidente> incidentesResueltos() {
    return entityManager().createQuery("SELECT i FROM Incidente i WHERE i.resuelto = true", Incidente.class)
        .getResultList();
  }
  public List<Incidente> incidentesNoResueltos() {
    return  entityManager().createQuery("SELECT i FROM Incidente i WHERE i.resuelto = false", Incidente.class)
        .getResultList();
  }
  public List<Incidente> incidentesDelServicio(Servicio servicio) {
    return  entityManager().createQuery("SELECT i FROM Incidente i WHERE i.servicio = :servicio", Incidente.class)
        .setParameter("servicio", servicio)
        .getResultList();
  }
  public List<Incidente> incidentesDelReportante(Usuario usuario) {
    return  entityManager().createQuery("SELECT i FROM Incidente i WHERE i.reportante = :usuario", Incidente.class)
        .setParameter("usuario", usuario)
        .getResultList();
  }
}
