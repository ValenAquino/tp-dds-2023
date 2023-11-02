package ar.edu.utn.frba.dds.model.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "establecimientos")
public class Establecimiento extends PersistentEntity {
  @OneToMany(mappedBy = "establecimiento", cascade = CascadeType.ALL)
  private final List<Servicio> servicios = new ArrayList<>();
  @ManyToOne
  private Entidad entidad;
  private String nombre;
  @Embedded
  private Ubicacion ubicacion;

  public Establecimiento(String nombre, Entidad entidad, Ubicacion ubicacion) {
    this.nombre = nombre;
    this.entidad = entidad;
    this.ubicacion = ubicacion;
  }

  public Establecimiento() { }

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

  public String getNombre() { return nombre; }
}
