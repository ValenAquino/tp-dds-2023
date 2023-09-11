package ar.edu.utn.frba.dds.entidades;

import java.util.ArrayList;
import java.util.List;

public class Establecimiento {
  private final List<Servicio> servicios = new ArrayList<>();
  private final Entidad entidad;
  private String nombre;
  private Ubicacion ubicacion;

  public Establecimiento(Entidad entidad) {
    this.entidad = entidad;
  }

  public void agregarServicio(Servicio servicio) {
    this.servicios.add(servicio);
  }

  public void removerServicio(Servicio servicio) {
    this.servicios.remove(servicio);
  }

  public boolean tieneServicio(Servicio servicio) {
    return servicios.contains(servicio);
  }

  public Entidad getEntidad() {
    return entidad;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }

}
