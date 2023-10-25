package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.entidades.Comunidad;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import javax.persistence.EntityTransaction;

public class RepositorioComunidades implements WithSimplePersistenceUnit {
  private static RepositorioComunidades instance;

  public static RepositorioComunidades getInstance() {
    if (instance == null) {
      instance = new RepositorioComunidades();
    }
    return instance;
  }

  public void persistir(Comunidad comunidad) {

    EntityTransaction transaction = entityManager().getTransaction();
    try {
      transaction.begin();
      entityManager().persist(comunidad);
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
    }
  }
  public List<Comunidad> getComunidadesDe(Usuario usuario) {
    return entityManager()
        .createQuery("select distinct c from Comunidad c join c.miembros m where m.id = :id", Comunidad.class)
        .setParameter("id", usuario.getId())
        .getResultList();
  }

  public List<Comunidad> todas() {
    return entityManager().createQuery("SELECT c FROM Comunidad", Comunidad.class)
        .getResultList();
  }
  public List<Comunidad> getComunidadesInteresadas(Usuario usuario, Servicio servicio) {
    return entityManager()
        .createQuery("SELECT c FROM Comunidad c JOIN c.serviciosDeInteres s WHERE :usuario MEMBER OF c.miembros AND :servicio MEMBER OF c.serviciosDeInteres", Comunidad.class)
        .setParameter("usuario", usuario.getId())
        .setParameter("servicio", servicio.getId())
        .getResultList();
  }
}
