package ar.edu.utn.frba.dds.entidades;

public class Localizacion {
  private String nombre;
  private double lat;
  private double lon;
  private TipoDeLocalizacion tipo;

  public Localizacion(String nombre, double lat, double lon, TipoDeLocalizacion tipo) {
    this.nombre = nombre;
    this.lat = lat;
    this.lon = lon;
    this.tipo = tipo;
  }

  public String getNombre() {
    return this.nombre;
  }
}
