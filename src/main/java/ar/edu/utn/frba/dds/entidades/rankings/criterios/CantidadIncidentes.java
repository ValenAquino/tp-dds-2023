package ar.edu.utn.frba.dds.entidades.rankings.criterios;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.rankings.CriterioDeOrdenamiento;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CantidadIncidentes implements CriterioDeOrdenamiento {

  @Override
  public String getDescripcion() {
    return "CantidadDeIncidentesReportados";
  }

  public Map<Entidad, Double> getEntidadesOrdenadas(List<Incidente> incidentes) {
    Map<Entidad, List<Incidente>> incidentesPorEntidad = agruparIncidentesPorEntidad(incidentes);

    return incidentesPorEntidad.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> calcularIncidentes(entry.getValue())
        ));
  }

  public Map<Entidad, List<Incidente>> agruparIncidentesPorEntidad(List<Incidente> incidentes) {
    return incidentes.stream().collect(Collectors.groupingBy(Incidente::getEntidad));
  }

  public double calcularIncidentes(List<Incidente> incidentes) {
    Map<Servicio, List<Incidente>> incidentesPorServicio = agruparIncidentesPorServicio(incidentes);

    Map<Servicio, Double> cantidadIncidentesPorServicio = incidentesPorServicio.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> calcularIncidentesPorServicio(entry.getValue())
        ));

    List<Double> cantidadesDeIncidentes = new ArrayList<>(cantidadIncidentesPorServicio.values());

    return cantidadesDeIncidentes.stream()
        .mapToDouble(Double::doubleValue)
        .sum();
  }

  public double calcularIncidentesPorServicio(List<Incidente> incidentes) {
    // Hago un stream de los indices desde el segundo al ultimo para iterar el acual con el anterior
    return IntStream.range(1, incidentes.size())
        .mapToDouble(i -> contarIncidentes(incidentes.get(i - 1), incidentes.get(i))).sum() + 1.0;
  }

  private Map<Servicio, List<Incidente>> agruparIncidentesPorServicio(List<Incidente> incidentes) {
    return incidentes.stream().collect(Collectors.groupingBy(Incidente::getServicio));
  }

  private double contarIncidentes(Incidente incidenteAnterior, Incidente incidenteActual) {
    // Si el anterior esta resuelto y el actual se abrio despues del cierre, se suma uno
    if (debeIncrementarPorResolucion(incidenteAnterior, incidenteActual)) {
      return 1.0;
    }

    // Si el anterior esta abierto y hay mas de 24hs de diferencia con el actual se suma uno
    if (pasaronMasDe24Horas(incidenteAnterior, incidenteActual)) {
      return 1.0;
    }

    return 0.0;
  }

  private boolean debeIncrementarPorResolucion(Incidente anterior, Incidente actual) {
    return anterior.estaResuelto() && anterior.getFechaResolucion().isBefore(actual.getFecha());
  }

  private boolean pasaronMasDe24Horas(Incidente anterior, Incidente actual) {
    Duration duracionEntreIncidentes = Duration.between(anterior.getFecha(), actual.getFecha());
    return !anterior.estaResuelto() && duracionEntreIncidentes.toHours() >= 24;
  }

}