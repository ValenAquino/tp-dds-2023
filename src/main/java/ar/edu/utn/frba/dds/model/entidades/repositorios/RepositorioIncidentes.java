package ar.edu.utn.frba.dds.model.entidades.repositorios;

import ar.edu.utn.frba.dds.model.entidades.Comunidad;
import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDateTime;
import java.util.List;

public class RepositorioIncidentes implements WithSimplePersistenceUnit {
  private static RepositorioIncidentes instance;

  public static RepositorioIncidentes getInstance() {
    if (instance == null) {
      instance = new RepositorioIncidentes();
    }
    return instance;
  }

  public void persistir(Incidente incidente) {
    entityManager().persist(incidente);
  }

  public List<Incidente> todos() {
    return entityManager().createQuery("SELECT i FROM Incidente i", Incidente.class)
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
        .setParameter("servicio", servicio.getId())
        .getResultList();
  }

  public List<Incidente> incidentesARevisarPara(Usuario usuario) {
    return entityManager().createQuery("SELECT i FROM Incidente i JOIN Notificacion n " +
            "ON n.incidente = i.id WHERE TYPE(n) = NotificacionRevisionIncidente AND " +
            "n.receptor = :receptor AND i.resuelto = false", Incidente.class)
        .setParameter("receptor", usuario)
        .getResultList();
  }

  public List<Incidente> incidentesARevisarPara(Usuario usuario, int n) {
    return entityManager().createQuery("SELECT i FROM Incidente i JOIN Notificacion n " +
                    "ON n.incidente = i.id WHERE TYPE(n) = NotificacionRevisionIncidente AND " +
                    "n.receptor = :receptor AND i.resuelto = false " +
                    "order by i.id", Incidente.class)
            .setParameter("receptor", usuario)
            .setMaxResults(n)
            .getResultList();
  }

  public List<Incidente> incidentesDelReportante(Usuario usuario) {
    return  entityManager().createQuery("SELECT i FROM Incidente i WHERE i.reportante = :usuario", Incidente.class)
        .setParameter("usuario", usuario)
        .getResultList();
  }

  public List<Incidente> incidentesUltimaSemana() {
    LocalDateTime ahora = LocalDateTime.now();
    LocalDateTime fechaLimite = ahora.minusDays(7);
    return  entityManager().createQuery("SELECT i FROM Incidente i WHERE i.fecha >= :fechaLimite AND i.fecha <= :ahora", Incidente.class)
        .setParameter("fechaLimite",fechaLimite)
        .setParameter("ahora",ahora)
        .getResultList();
  }
  public Incidente porId(Integer id) {
    return entityManager().find(Incidente.class, id);
  }
}