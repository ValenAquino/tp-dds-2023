package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Servicio extends PersistentEntity {
  private final String descripcion;
  @Enumerated(value = EnumType.STRING)
  private final TipoDeServicio tipoDeServicio;
  @Transient
  private Establecimiento establecimiento;

  public Servicio(String descripcion, TipoDeServicio tipoDeServicio) {
    this.descripcion = descripcion;
    this.tipoDeServicio = tipoDeServicio;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public TipoDeServicio getTipoDeServicio() {
    return tipoDeServicio;
  }

  public Ubicacion getUbicacion() {
    return establecimiento.getUbicacion();
  }

  public Entidad getEntidad() {
    return establecimiento.getEntidad();
  }

  public void setEstablecimiento(Establecimiento establecimiento) {
    this.establecimiento = establecimiento;
  }
}
