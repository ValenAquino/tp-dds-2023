package ar.edu.utn.frba.dds.entidades;

import javax.persistence.Embeddable;

@Embeddable
public class Ubicacion {
  private double latitud;
  private double longitud;

  public Ubicacion(double latitud, double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public Ubicacion() { }

  public double getLatitud() {
    return latitud;
  }

  public double getLongitud() {
    return longitud;
  }
}