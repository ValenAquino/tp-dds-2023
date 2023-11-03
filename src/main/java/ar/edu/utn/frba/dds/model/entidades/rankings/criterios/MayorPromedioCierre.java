package ar.edu.utn.frba.dds.model.entidades.rankings.criterios;

import ar.edu.utn.frba.dds.model.entidades.Entidad;
import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.rankings.CriterioDeOrdenamiento;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MayorPromedioCierre implements CriterioDeOrdenamiento {
  @Override
  public Map<Entidad, Double> getEntidadesOrdenadas(List<Incidente> incidentes) {
    var entidades = incidentes
        .stream()
        .filter(Incidente::estaResuelto)
        .collect(Collectors
            .groupingBy(
                Incidente::getEntidad,
                Collectors.averagingDouble(Incidente::tiempoDeCierre)));

    return entidades
        .entrySet()
        .stream()
        .sorted(Map.Entry.<Entidad, Double>comparingByValue().reversed()) // Ordenar de mayor a menor
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldValue, newValue) -> oldValue,
                LinkedHashMap::new)); // Usar LinkedHashMap para mantener el orden

  }

  @Override
  public String getDescripcion() {
    return "Tiempo de cierre promedio en minutos";
  }
}
