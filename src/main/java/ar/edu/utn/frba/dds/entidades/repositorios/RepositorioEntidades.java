package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Servicio;
import java.util.List;

public class RepositorioEntidades {
  private static RepositorioEntidades instance;
  private List<Entidad> entidades;

  public List<Entidad> todas() {
    return this.entidades;
  }

  public static RepositorioEntidades getInstance() {
    if (instance == null) {
      instance = new RepositorioEntidades();
    }
    return instance;
  }

  public Entidad getEntidadDe(Servicio servicio) {
    return todas().stream()
        .filter(e -> e.tieneServicio(servicio))
        .findFirst()
        .orElse(null);
  }

}
