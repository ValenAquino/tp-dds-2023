package ar.edu.utn.frba.dds.entidades;

import java.util.ArrayList;
import java.util.List;

public class Servicio {
  private final String descripcion;
  private TipoDeServicio tipoDeServicio;

  public Servicio(String descripcion, TipoDeServicio tipoDeServicio) {
    this.descripcion = descripcion;
    this.tipoDeServicio = tipoDeServicio;
  }

  public String getDescripcion() {
    return descripcion;
  }
}
