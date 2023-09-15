package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.entidades.Comunidad;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Usuario;
import java.util.List;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepositorioComunidades implements WithGlobalEntityManager {
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
    return entityManager().createQuery("from comunidades").getResultList();
  }

  public List<Comunidad> getComunidadesDe(Usuario usuario) {
    return todas().stream()
        .filter(c -> c.tieneMiembro(usuario))
        .toList();

    // TODO: Implementar con query --> Borrar todas()
  }

  public List<Comunidad> getComunidadesInteresadas(Usuario usuario, Servicio servicio) {
    return getComunidadesDe(usuario).stream()
        .filter(c -> c.leInteresaServicio(servicio))
        .toList();

    // TODO: Implementar con query
  }
}
