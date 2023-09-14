package ar.edu.utn.frba.dds.entidades;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Incidente extends PersistentEntity {
  boolean resuelto;
  LocalDateTime fecha;
  LocalDateTime fechaResolucion;
  @ManyToOne
  Servicio servicio;
  String observaciones;
  @ManyToOne
  Usuario reportante;

  public Incidente(Servicio servicio, String observaciones) {
    this.resuelto = false;
    this.fecha = LocalDateTime.now();
    this.servicio = servicio;
    this.observaciones = observaciones;
  }

  public Incidente(Servicio servicio, String observaciones, LocalDateTime fecha,
                   Usuario reportante) {
    this(servicio, observaciones);
    this.fecha = fecha;
    this.reportante = reportante;
  }

  public boolean estaResuelto() {
    return resuelto;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }

  public LocalDateTime getFechaResolucion() {
    return fechaResolucion;
  }

  public Servicio getServicio() {
    return servicio;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public Usuario getReportante() {
    return reportante;
  }

  public void cerrar() {
    resuelto = true;
    fechaResolucion = LocalDateTime.now();
  }

  public long tiempoDeCierre() {
    return Duration.between(fecha, fechaResolucion).toMillis();
  }

  public Entidad getEntidad() {
    return servicio.getEntidad();
  }

  public Ubicacion getUbicacion() {
    return servicio.getUbicacion();
  }
}
