package ar.edu.utn.frba.dds.entidades;

public class LineaDeTransporte extends Entidad {
  private TipoDeTransporte tipo;

  public LineaDeTransporte(String nombre, Localizacion localizacion, TipoDeTransporte tipo) {
    super(nombre, localizacion);
    this.tipo = tipo;
  }
}
