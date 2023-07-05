package ar.edu.utn.frba.dds.entidades.rankings.criterios;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.rankings.CriterioDeOrdenamiento;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;

public class MayorPromedioCierre extends CriterioDeOrdenamiento {
  public MayorPromedioCierre(String nombre) {
    super("Mayor promedio de cierre");
  }

  @Override
  public List<Entidad> ordenar(List<Entidad> entidades) {
    entidades.sort(Comparator.comparingDouble(this::calcularPromedioCierre).reversed());
    return entidades;
  }

  private double calcularPromedioCierre(Entidad entidad) {
    List<Incidente> incidentes = entidad.getIncidentes();

    return incidentes.stream()
        .filter(Incidente::estaResuelto)
        .mapToLong(incidente ->
            Duration.between(incidente.getFecha(), incidente.getFechaResolucion()).toMillis())
        .average()
        .orElse(0.0);
  }
}
