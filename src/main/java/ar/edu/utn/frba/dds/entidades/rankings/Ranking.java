package ar.edu.utn.frba.dds.entidades.rankings;

import ar.edu.utn.frba.dds.entidades.Entidad;
import java.time.LocalDateTime;
import java.util.List;

public class Ranking {
  LocalDateTime fecha;
  CriterioDeOrdenamiento criterio;
  List<Entidad> entidades;

  public Ranking(CriterioDeOrdenamiento criterioElegido) {
    this.fecha = LocalDateTime.now();
    this.criterio = criterioElegido;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }

  public void generarRanking() {
    entidades = criterio.getEntidadesOrdenadas();
  }

  public List<Entidad> getEntidades() {
    return entidades;
  }
}