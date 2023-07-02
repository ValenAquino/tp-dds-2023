package ar.edu.utn.frba.dds.entidades;

import java.time.LocalDateTime;

public class Incidente {
  boolean resuelto;
  LocalDateTime fecha;
  Servicio servicio;
  String observaciones;

  public Incidente(Servicio servicio, String observaciones) {
    this.resuelto = false;
    this.fecha = LocalDateTime.now();
    this.servicio = servicio;
    this.observaciones = observaciones;
  }
}
