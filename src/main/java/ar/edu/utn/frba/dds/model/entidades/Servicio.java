package ar.edu.utn.frba.dds.model.entidades;

import ar.edu.utn.frba.dds.model.entidades.enums.TipoDeServicio;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "servicios")
public class Servicio extends PersistentEntity {
  private String descripcion;
  @Enumerated(value = EnumType.STRING)
  @Column(name = "tipo")
  private TipoDeServicio tipoDeServicio;

  @ManyToOne
  private Establecimiento establecimiento;

  public Servicio(String descripcion, TipoDeServicio tipoDeServicio) {
    this.descripcion = descripcion;
    this.tipoDeServicio = tipoDeServicio;
  }

  public Servicio() { }

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

  public Establecimiento getEstablecimiento() { return establecimiento; }

  public void setEstablecimiento(Establecimiento establecimiento) {
    this.establecimiento = establecimiento;
  }
}
