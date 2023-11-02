package ar.edu.utn.frba.dds.model.entidades.repositorios;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class RepositorioUsuarios implements WithSimplePersistenceUnit {
  private static RepositorioUsuarios instance;

  public static RepositorioUsuarios getInstance() {
    if (instance == null) {
      instance = new RepositorioUsuarios();
    }
    return instance;
  }

  public void persistir(Usuario usuario) {
    entityManager().persist(usuario);
  }

  public Usuario porUsuarioYContrasenia(String usuario, String contrasenia) {
    return entityManager()
        .createQuery("from Usuario where usuario = :usuario and contrasenia = :contrasenia", Usuario.class)
        .setParameter("usuario", usuario)
        .setParameter("contrasenia", contrasenia)
        .getResultList()
        .get(0);
  }

  public Usuario porId(Integer id) {
    return entityManager().find(Usuario.class, id);
  }

}
