package ar.edu.utn.frba.dds.entidades.rankings.criterios;
import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.rankings.CriterioDeOrdenamiento;
import java.util.List;

public class CantidadIncidentes extends CriterioDeOrdenamiento {

  public CantidadIncidentes(String nombre) {
    super(nombre);
  }

  @Override
  public void ordenar(List<Entidad> entidades) {

  }
}
