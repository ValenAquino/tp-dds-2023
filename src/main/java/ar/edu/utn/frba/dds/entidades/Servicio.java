package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;

public class Servicio {
  private final String descripcion;
  private final TipoDeServicio tipoDeServicio;
  private Ubicacion ubicacion;

  public Servicio(String descripcion, TipoDeServicio tipoDeServicio) {
    this.descripcion = descripcion;
    this.tipoDeServicio = tipoDeServicio;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public TipoDeServicio getTipoDeServicio() {
    return tipoDeServicio;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }
}
