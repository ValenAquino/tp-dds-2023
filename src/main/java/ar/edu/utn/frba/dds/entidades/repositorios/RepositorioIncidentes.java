package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.entidades.Incidente;
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

  @SuppressWarnings("unchecked")
  public List<Incidente> todos() {
    return entityManager().createQuery("from incidentes").getResultList();
  }

  public List<Incidente> ultimaSemana() {
    LocalDateTime ahora = LocalDateTime.now();
    LocalDateTime fechaLimite = ahora.minusDays(7);

    return todos().stream()
        .filter(i -> i.getFecha().isAfter(fechaLimite) && i.getFecha().isBefore(ahora))
        .collect(Collectors.toList());

    // TODO: Implementar con query --> Borrar todos()
  }
}
