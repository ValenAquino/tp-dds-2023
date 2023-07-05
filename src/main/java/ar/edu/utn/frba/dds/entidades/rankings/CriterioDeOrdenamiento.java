package ar.edu.utn.frba.dds.entidades.rankings;

import ar.edu.utn.frba.dds.entidades.Entidad;
import java.util.List;

abstract public class CriterioDeOrdenamiento {
  private final String nombre;

  public CriterioDeOrdenamiento(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return nombre;
  }

  abstract public void ordenar(List<Entidad> entidades);
}
