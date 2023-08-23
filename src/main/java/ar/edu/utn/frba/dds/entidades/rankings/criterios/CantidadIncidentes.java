package ar.edu.utn.frba.dds.entidades.rankings.criterios;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.rankings.CriterioDeOrdenamiento;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

  private Map<Servicio, List<Incidente>> agruparIncidentesPorServicio(List<Incidente> incidentes) {
    incidentes.sort(Comparator.comparing(Incidente::getFecha));
    return incidentes.stream().collect(Collectors.groupingBy(Incidente::getServicio));
  }

  public double calcularIncidentesPorServicio(List<Incidente> incidentes) {
    double cantidadIncidentes = 1.0; // No puede llegar con 0
    LocalDateTime ultimoCierre = null;

    for (int i = 1; i < incidentes.size(); i++) {
      Incidente anterior = incidentes.get(i - 1);
      Incidente actual = incidentes.get(i);

      if (anterior.estaResuelto()) {
        ultimoCierre = anterior.getFechaResolucion();
      }

      cantidadIncidentes += contarIncidentes(anterior, actual, ultimoCierre);
    }

    return cantidadIncidentes;
  }

  private double contarIncidentes(Incidente anterior, Incidente actual, LocalDateTime ultCierre) {
    // Si se abrio después del ultimo cierre, se suma 1
    if(sumaPorCierre(anterior, actual, ultCierre)) {
      return 1.0;
    }

    // Si el anterior esta abierto y hay mas de 24hs de diferencia con el actual se suma uno
    if (pasaronMasDe24Horas(anterior, actual)) {
      return 1.0;
    }

    return 0.0;
  }

  private boolean sumaPorCierre(Incidente anterior, Incidente actual, LocalDateTime ultCierre) {
    return ultCierre != null
        && !anterior.getFecha().isAfter(ultCierre) // No se abrio antes del ultimo cierre
        && actual.getFecha().isAfter(ultCierre); // Se abrio despues del ultimo cierre
  }

  private boolean pasaronMasDe24Horas(Incidente anterior, Incidente actual) {
    Duration duracionEntreIncidentes = Duration.between(anterior.getFecha(), actual.getFecha());
    return !anterior.estaResuelto() && duracionEntreIncidentes.toHours() >= 24;
  }

}