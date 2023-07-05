package ar.edu.utn.frba.dds.entidades;

import java.util.ArrayList;
import java.util.List;

public class Servicio {
  List<Incidente> incidentes;
  private final String descripcion;
  private TipoDeServicio tipoDeServicio;

  public Servicio(String descripcion, TipoDeServicio tipoDeServicio) {
    this.descripcion = descripcion;
    this.tipoDeServicio = tipoDeServicio;
    this.incidentes = new ArrayList<>();
  }

  public void agregarIncidente(Incidente incidente) {
    incidentes.add(incidente);
  }

  public List<Incidente> getIncidentesPorFecha() {
    return incidentes;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public List<Incidente> getIncidentes() {
    return incidentes;
  }
}
