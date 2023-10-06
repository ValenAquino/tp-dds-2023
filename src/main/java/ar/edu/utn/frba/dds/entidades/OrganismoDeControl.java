package ar.edu.utn.frba.dds.entidades;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "organismos_de_control")
public class OrganismoDeControl extends PersistentEntity {
  private String nombre;
  @Column(name = "correo_electronico")
  private String correoElectronico;
  @ManyToMany
  @JoinTable(
      name = "entidades_controladas_por_organismos_de_control",
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

  public OrganismoDeControl() { }

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
