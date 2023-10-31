package ar.edu.utn.frba.dds.model.entidades.rankings;

import ar.edu.utn.frba.dds.model.entidades.Entidad;
import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Ranking {
  final LocalDateTime fecha;
  final CriterioDeOrdenamiento criterio;
  final RepositorioIncidentes repo;
  Map<Entidad, Double> entidades;

  public Ranking(RepositorioIncidentes repo, CriterioDeOrdenamiento criterio) {
    this.fecha = LocalDateTime.now();
    this.criterio = criterio;
    this.repo = repo;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }

  public void generarRanking() {
    List<Incidente> incidentesUltimaSemana = repo.ultimaSemana();
    this.entidades = criterio.getEntidadesOrdenadas(incidentesUltimaSemana);
  }

  public String getDescripcionCriterio() {
    return criterio.getDescripcion();
  }

  public Map<Entidad, Double> getEntidades() {
    return entidades;
  }
}