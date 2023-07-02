package ar.edu.utn.frba.dds.entidades;

public class Servicio {
  private String descripcion;
  private TipoDeServicio tipoDeServicio;

  public Servicio(String descripcion, TipoDeServicio tipoDeServicio) {
    this.descripcion = descripcion;
    this.tipoDeServicio = tipoDeServicio;
  }
}
