package ar.edu.utn.frba.dds.entidades;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class OrganismoDeControl extends PersistentEntity {
  private final String nombre;
  private final String correoElectronico;
  @ManyToMany
  @JoinTable(
      joinColumns = @JoinColumn(name = "organismo_id"),
      inverseJoinColumns = @JoinColumn(name = "entidad_id")
  )
  private final Set<Entidad> entidadesControladas = new HashSet<>();
  @ManyToOne
  @JoinColumn(name = "responsable_id")
  private Usuario usuarioResponsable;

  public OrganismoDeControl(String nombre, String correoElectronico) {
    this.nombre = nombre;
    this.correoElectronico = correoElectronico;
  }

  public String getNombre() {
    return nombre;
  }

  public String getCorreoElectronico() {
    return correoElectronico;
  }

  public void setResponsable(Usuario nuevoResponsable) {
    usuarioResponsable = nuevoResponsable;
  }

  public void agregarEntidad(Entidad nuevaEntidad) {
    entidadesControladas.add(nuevaEntidad);
  }

  public Set<Entidad> getEntidades() {
    return entidadesControladas;
  }
}
