package ar.edu.utn.frba.dds.model.entidades;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidentes")
public class Incidente extends PersistentEntity {
  boolean resuelto;
  LocalDateTime fecha;
  @Column(name = "fecha_resolucion")
  LocalDateTime fechaResolucion;
  @ManyToOne
  Servicio servicio;
  String observaciones;
  @ManyToOne
  Usuario reportante;

  public Incidente(Servicio servicio, String observaciones, LocalDateTime fecha) {
    this.resuelto = false;
    this.fecha = fecha;
    this.servicio = servicio;
    this.observaciones = observaciones;
  }

  public Incidente(Servicio servicio, String observaciones, LocalDateTime fecha,
                   Usuario reportante) {
    this(servicio, observaciones, fecha);
    this.reportante = reportante;
  }

  public Incidente() { }

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

  public void setReportante(Usuario reportante) {
    this.reportante = reportante;
  }

  public void cerrar(LocalDateTime fecha) {
    resuelto = true;
    fechaResolucion = fecha;
  }

  public long tiempoDeCierre() {
    return Duration.between(fecha, fechaResolucion).toMinutes();
  }

  public Entidad getEntidad() {
    return servicio.getEntidad();
  }

  public Ubicacion getUbicacion() {
    return servicio.getUbicacion();
  }

  public Establecimiento getEstablecimiento() {
    return servicio.getEstablecimiento();
  }
}
