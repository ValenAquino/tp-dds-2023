package ar.edu.utn.frba.dds.model.entidades.repositorios;

import ar.edu.utn.frba.dds.model.entidades.Comunidad;
import ar.edu.utn.frba.dds.model.entidades.Entidad;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepositorioEntidades implements WithSimplePersistenceUnit {
  private static RepositorioEntidades instance;

  public static RepositorioEntidades getInstance() {
    if (instance == null) {
      instance = new RepositorioEntidades();
    }
    return instance;
  }

  public void persistir(Entidad entidad) {
    entityManager().persist(entidad);
  }
}
