package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.entidades.Comunidad;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Usuario;
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

  @SuppressWarnings("unchecked")
  public List<Comunidad> todas() {
    return entityManager().createQuery("from Comunidad").getResultList();
  }

  public List<Comunidad> getComunidadesDe(Usuario usuario) {
    return entityManager()
        .createQuery("select distinct c from Comunidad c join c.miembros m where m.id = :id", Comunidad.class)
        .setParameter("id", usuario.getId())
        .getResultList();
  }

  public List<Comunidad> getComunidadesInteresadas(Usuario usuario, Servicio servicio) {
    return getComunidadesDe(usuario).stream()
        .filter(c -> c.leInteresaServicio(servicio))
        .toList();

    // TODO: Implementar con query
  }
}
