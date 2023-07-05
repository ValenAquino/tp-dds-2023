package ar.edu.utn.frba.dds.entidades.rankings.criterios;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.rankings.CriterioDeOrdenamiento;
import java.util.Comparator;
import java.util.List;

public class CantidadIncidentes extends CriterioDeOrdenamiento {

  public CantidadIncidentes(String nombre) {
    super("Cantidad de incidentes");
  }

  @Override
  public List<Entidad> ordenar(List<Entidad> entidades) {
    // Obtener los de la ultima semana
    // Filtrar que haya uno por dia
    // Si hay mas de uno por día, solo uno puede seguir abierto o ninguno abierto? 🤔
    entidades.sort(Comparator.comparingInt((Entidad e) -> e.getIncidentes().size()).reversed());
    return entidades;
  }
}
