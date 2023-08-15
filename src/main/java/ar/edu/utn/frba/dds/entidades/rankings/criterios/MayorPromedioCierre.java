package ar.edu.utn.frba.dds.entidades.rankings.criterios;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.rankings.CriterioDeOrdenamiento;
import java.util.HashMap;
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
        .sorted(Map.Entry.comparingByValue())
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldValue, newValue) -> oldValue,
                HashMap::new));
  }

  @Override
  public String getDescripcion() {
    return "PromedioTiempoCierreDeIncidentes";
  }
}
