package ar.edu.utn.frba.dds.entidades.rankings.criterios;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.rankings.CriterioDeOrdenamiento;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MayorPromedioCierre extends CriterioDeOrdenamiento {
  public MayorPromedioCierre() {
    super("Mayor promedio de cierre");
  }

  @Override
  public List<Entidad> ordenar(List<Entidad> entidades) {
    entidades.sort(Comparator.comparingDouble(this::calcularPromedioCierre).reversed());
    return entidades;
  }

  private double calcularPromedioCierre(Entidad entidad) {
    List<Incidente> incidentesResueltos = obtenerIncidentesResueltos(entidad);

    if (incidentesResueltos.isEmpty()) {
      // Excepcion?
      return 0.0;
    }

    long duracionTotal = calcularDuracionTotal(incidentesResueltos);
    return (double) duracionTotal / incidentesResueltos.size();
  }

  private List<Incidente> obtenerIncidentesResueltos(Entidad entidad) {
    LocalDateTime fechaActual = LocalDateTime.now();

    return entidad.getIncidentesSemana(fechaActual).stream()
        .filter(Incidente::estaResuelto)
        .collect(Collectors.toList());
  }

  private long calcularDuracionTotal(List<Incidente> incidentes) {
    return incidentes.stream()
        .mapToLong(this::calcularDuracionIncidente)
        .sum();
  }

  private long calcularDuracionIncidente(Incidente incidente) {
    LocalDateTime fechaInicio = incidente.getFecha();
    LocalDateTime fechaResolucion = incidente.getFechaResolucion();

    return Duration.between(fechaInicio, fechaResolucion).toMillis();
  }


}
