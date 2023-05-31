package ar.edu.utn.frba.dds.localizacion;

public class Localizacion {
  private String nombre;
  private double lat;
  private double lon;

  public Localizacion(String nombre, Double lat, Double lon) {
    this.nombre = nombre;
    this.lat = lat;
    this.lon = lon;
  }
}
