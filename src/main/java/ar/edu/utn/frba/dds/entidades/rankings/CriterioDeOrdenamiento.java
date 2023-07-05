package ar.edu.utn.frba.dds.entidades.rankings;

import ar.edu.utn.frba.dds.entidades.Entidad;
import java.util.List;

public abstract class CriterioDeOrdenamiento {
  private final String nombre;

  public CriterioDeOrdenamiento(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  public abstract List<Entidad> ordenar(List<Entidad> entidades);
}
