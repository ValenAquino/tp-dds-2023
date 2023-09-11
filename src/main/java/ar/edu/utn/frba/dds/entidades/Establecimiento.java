package ar.edu.utn.frba.dds.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Establecimiento extends PersistentEntity {
  @OneToMany(mappedBy = "establecimiento")
  private final List<Servicio> servicios = new ArrayList<>();
  @ManyToOne
  private final Entidad entidad;
  private String nombre;
  @Embedded
  private final Ubicacion ubicacion;

  public Establecimiento(Entidad entidad, Ubicacion ubicacion) {
    this.entidad = entidad;
    this.ubicacion = ubicacion;
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
