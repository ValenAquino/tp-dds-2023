package ar.edu.utn.frba.dds.entidades.rankings.criterios;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.rankings.CriterioDeOrdenamiento;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioEntidades;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioIncidentes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CantidadIncidentes implements CriterioDeOrdenamiento {

  @Override
  public Map<Entidad, Double> getEntidadesOrdenadas() {
    var entidades = RepositorioIncidentes.getInstance()
        .ultimaSemana()
        .stream()
        .collect(Collectors
            .groupingBy(incidente ->
                RepositorioEntidades.getInstance().getEntidadDe(incidente.getServicio()),
                Collectors.counting()));

    return entidades
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                v -> (double)v.getValue(),
                (oldValue, newValue) -> oldValue,
                HashMap::new));
  }
}
