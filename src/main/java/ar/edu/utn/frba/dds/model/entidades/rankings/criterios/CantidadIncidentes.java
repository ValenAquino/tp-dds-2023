package ar.edu.utn.frba.dds.model.entidades.rankings.criterios;

import ar.edu.utn.frba.dds.model.entidades.Entidad;
import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.rankings.CriterioDeOrdenamiento;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CantidadIncidentes implements CriterioDeOrdenamiento {

  @Override
  public String getDescripcion() {
    return "Cantidad de incidentes reportados";
  }

  public Map<Entidad, Double> getEntidadesOrdenadas(List<Incidente> incidentes) {
    Map<Entidad, List<Incidente>> incidentesPorEntidad = incidentes.stream()
        .collect(Collectors.groupingBy(Incidente::getEntidad));

    return incidentesPorEntidad.entrySet().stream()
        .sorted(Comparator
            .comparing((Map.Entry<Entidad, List<Incidente>> entry) -> calcularIncidentes(entry.getValue()))
            .reversed()) // Ordenar en orden descendente
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> calcularIncidentes(entry.getValue()),
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));

  }


  public double calcularIncidentes(List<Incidente> incidentes) {
    Map<Servicio, List<Incidente>> incidentesPorServicio = incidentes.stream()
        .sorted(Comparator.comparing(Incidente::getFecha))
        .collect(Collectors.groupingBy(Incidente::getServicio));

    Map<Servicio, Double> cantidadIncidentesPorServicio = incidentesPorServicio.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> calcularIncidentesPorServicio(entry.getValue())
        ));

    return cantidadIncidentesPorServicio.values().stream()
        .mapToDouble(Double::doubleValue)
        .sum();
  }

  public double calcularIncidentesPorServicio(List<Incidente> incidentes) {
    double cantidadIncidentes = 1.0;
    LocalDateTime ultimoCierre = null;

    for (int i = 1; i < incidentes.size(); i++) {
      Incidente anterior = incidentes.get(i - 1);
      Incidente actual = incidentes.get(i);

      if (anterior.estaResuelto()) {
        ultimoCierre = anterior.getFechaResolucion();
      }

      /*
       Se cuenta el incidente
       si se abrio después del último cierre o
       si el anterior está abierto y hay más de 24 hs de diferencia con el actual
      */
      if (seAbrioLuegoDelUltimoCierre(anterior, actual, ultimoCierre) ||
          pasaronMasDe24Horas(anterior, actual)) {
        cantidadIncidentes++;
      }
    }

    return cantidadIncidentes;
  }

  private boolean seAbrioLuegoDelUltimoCierre(Incidente anterior, Incidente actual,
                                              LocalDateTime ultimoCierre) {
    return ultimoCierre != null
        && !anterior.getFecha().isAfter(ultimoCierre)
        && actual.getFecha().isAfter(ultimoCierre);
  }

  private boolean pasaronMasDe24Horas(Incidente anterior, Incidente actual) {
    return !anterior.estaResuelto() &&
        Duration.between(anterior.getFecha(), actual.getFecha()).toHours() >= 24;
  }

}