package ar.edu.utn.frba.dds.entidades;

import javax.persistence.Embeddable;

@Embeddable
public class Ubicacion {
  private final double latitud;
  private final double longitud;

  public Ubicacion(double latitud, double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public double getLatitud() {
    return latitud;
  }

  public double getLongitud() {
    return longitud;
  }
}