package ar.edu.utn.frba.dds.entidades;

import java.time.LocalDateTime;

public class Incidente {
  boolean resuelto;
  LocalDateTime fecha;
  LocalDateTime fechaResolucion;
  Servicio servicio;
  String observaciones;

  public Incidente(Servicio servicio, String observaciones) {
    this.resuelto = false;
    this.fecha = LocalDateTime.now();
    this.servicio = servicio;
    this.observaciones = observaciones;
  }

  public void cerrar() {
    resuelto = true;
    fechaResolucion = LocalDateTime.now();
  }

  public boolean estaResuelto() {
    return resuelto;
  }

  public LocalDateTime getFechaResolucion() {
    return fechaResolucion;
  }
}
