package ar.edu.utn.frba.dds.model.entidades.repositorios;

import ar.edu.utn.frba.dds.model.entidades.Comunidad;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepositorioComunidades implements WithSimplePersistenceUnit {
  private static RepositorioComunidades instance;

  public static RepositorioComunidades getInstance() {
    if (instance == null) {
      instance = new RepositorioComunidades();
    }
    return instance;
  }

  public void persistir(Comunidad comunidad) {
    entityManager().persist(comunidad);
  }
  public List<Comunidad> comunidadesDeUsuario(Usuario usuario) {
    return entityManager()
        .createQuery("select distinct c from Comunidad c join c.miembros m where m.id = :id", Comunidad.class)
        .setParameter("id", usuario.getId())
        .getResultList();
  }

  public List<Comunidad> comunidadesDeUsuario(Usuario usuario, int n) {
    return entityManager()
            .createQuery("select distinct c from Comunidad c join c.miembros m where m.id = :id " +
                    "order by c.id", Comunidad.class)
            .setParameter("id", usuario.getId())
            .setMaxResults(n)
            .getResultList();
  }

  public List<Comunidad> todas() {
    return entityManager().createQuery("SELECT c FROM Comunidad c", Comunidad.class)
        .getResultList();
  }

  public List<Comunidad> comunidadesPorUsuario(Usuario usuario) {
    return entityManager()
        .createQuery("SELECT c FROM Comunidad c WHERE :usuario MEMBER OF c.miembros", Comunidad.class)
        .setParameter("usuario", usuario)
        .getResultList();
  }

  public List<Comunidad> comunidadesInteresadas(Usuario usuario, Servicio servicio) {
    return entityManager()
        .createQuery("SELECT distinct c FROM Comunidad c JOIN c.serviciosDeInteres s WHERE :usuario MEMBER OF c.miembros AND :servicio MEMBER OF c.serviciosDeInteres", Comunidad.class)
        .setParameter("usuario", usuario)
        .setParameter("servicio", servicio)
        .getResultList();
  }

  public Comunidad porId(Integer id) {
    return entityManager().find(Comunidad.class, id);
  }

  public void eliminar(Comunidad comunidad) {
    entityManager().remove(comunidad);
  }
}
