package ar.edu.utn.frba.dds.model.entidades.repositorios;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.Collection;

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

  public Collection<Usuario> todos() {
    return entityManager().createQuery("from Usuario", Usuario.class).getResultList();
  }

  public Usuario porUsuarioYContrasenia(String usuario, String contrasenia) {
    return entityManager()
        .createQuery("from Usuario where usuario = :usuario and contrasenia = :contrasenia", Usuario.class)
        .setParameter("usuario", usuario)
        .setParameter("contrasenia", DigestUtils.sha256Hex(contrasenia))
        .getResultList()
        .get(0);
  }

  public Usuario porId(Integer id) {
    return entityManager().find(Usuario.class, id);
  }

  public void eliminar(Usuario usuario) {
    entityManager().remove(usuario);
  }
}
