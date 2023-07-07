package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.entidades.Comunidad;
import ar.edu.utn.frba.dds.entidades.Usuario;
import java.util.List;

public class RepositorioComunidades {
  private static RepositorioComunidades instance;
  private List<Comunidad> comunidades;

  public List<Comunidad> todas() {
    return this.comunidades;
  }

  public static RepositorioComunidades getInstance() {
    if (instance == null) {
      instance = new RepositorioComunidades();
    }
    return instance;
  }

  public List<Comunidad> getComunidadesDe(Usuario usuario) {
    return todas().stream()
        .filter(c -> c.tieneMiembro(usuario))
        .toList();
  }
}