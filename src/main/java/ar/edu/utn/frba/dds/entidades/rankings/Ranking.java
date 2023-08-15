package ar.edu.utn.frba.dds.entidades.rankings;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioIncidentes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Ranking {
  LocalDateTime fecha;
  CriterioDeOrdenamiento criterio;
  Map<Entidad, Double> entidades;

  public Ranking(CriterioDeOrdenamiento criterio) {
    this.fecha = LocalDateTime.now();
    this.criterio = criterio;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }

  public void generarRanking() {
    List<Incidente> incidentesUltimaSemana = RepositorioIncidentes.getInstance().ultimaSemana();
    this.entidades = criterio.getEntidadesOrdenadas(incidentesUltimaSemana);
  }

  public String getDescripcionCriterio() {
    return criterio.getDescripcion();
  }

  public Map<Entidad, Double> getEntidades() {
    return entidades;
  }
}