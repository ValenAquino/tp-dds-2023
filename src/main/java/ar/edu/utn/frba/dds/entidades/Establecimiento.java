package ar.edu.utn.frba.dds.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Establecimiento extends PersistentEntity {
  @Transient
  private final List<Servicio> servicios = new ArrayList<>();
  @Transient
  private final Entidad entidad;
  private String nombre;
  @Embedded
  private Ubicacion ubicacion;

  public Establecimiento(Entidad entidad) {
    this.entidad = entidad;
  }

  public void agregarServicio(Servicio servicio) {
    this.servicios.add(servicio);
  }

  public void removerServicio(Servicio servicio) {
    this.servicios.remove(servicio);
  }

  public boolean tieneServicio(Servicio servicio) {
    return servicios.contains(servicio);
  }

  public Entidad getEntidad() {
    return entidad;
  }

  public Ubicacion getUbicacion() {
    return ubicacion;
  }

}
