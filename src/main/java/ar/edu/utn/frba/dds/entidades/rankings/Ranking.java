package ar.edu.utn.frba.dds.entidades.rankings;

import ar.edu.utn.frba.dds.entidades.Entidad;
import java.time.LocalDateTime;
import java.util.List;

public class Ranking {
  LocalDateTime fecha;
  String criterioElegido;
  List<Entidad> entidades;

  public Ranking(LocalDateTime fecha, String criterioElegido, List<Entidad> entidades) {
    this.fecha = fecha;
    this.criterioElegido = criterioElegido;
    this.entidades = entidades;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }

  public String getCriterioElegido() {
    return criterioElegido;
  }

  public List<Entidad> getEntidades() {
    return entidades;
  }
}
