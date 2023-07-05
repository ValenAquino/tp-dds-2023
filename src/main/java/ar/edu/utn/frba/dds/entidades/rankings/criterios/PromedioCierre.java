package ar.edu.utn.frba.dds.entidades.rankings.criterios;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.rankings.CriterioDeOrdenamiento;
import java.util.List;

public class PromedioCierre extends CriterioDeOrdenamiento {
  public PromedioCierre(String nombre) {
    super(nombre);
  }

  @Override
  public void ordenar(List<Entidad> entidades) {

  }
}
