package ar.edu.utn.frba.dds.entidades;

import java.util.ArrayList;
import java.util.List;

public class Establecimiento {
  private String nombre;
  private final List<Servicio> servicios = new ArrayList<>();

  public void agregarServicio(Servicio servicio) {
    this.servicios.add(servicio);
  }

  public void removerServicio(Servicio servicio) {
    this.servicios.remove(servicio);
  }

  public List<Incidente> getIncidentes() {
    List<Incidente> incidentes = new ArrayList<>();
    for (Servicio servicio : servicios) {
      incidentes.addAll(servicio.getIncidentes());
    }
    return incidentes;
  }
}
