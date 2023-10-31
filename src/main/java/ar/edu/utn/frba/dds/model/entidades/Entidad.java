package ar.edu.utn.frba.dds.model.entidades;

import ar.edu.utn.frba.dds.model.entidades.enums.TipoDeEntidad;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "entidades")
public class Entidad extends PersistentEntity {
  @OneToMany(mappedBy = "entidad")
  private final List<Establecimiento> establecimientos = new ArrayList<>();
  @ManyToMany
  @JoinTable(
      name = "usuarios_interesados_por_entidades",
      joinColumns = @JoinColumn(name = "entidad_id"),
      inverseJoinColumns = @JoinColumn(name = "usuario_id")
  )
  private final List<Usuario> usuariosInteresados = new ArrayList<>();
  @OneToMany
  @JoinColumn(name = "entidad_id")
  private final List<Incidente> incidentes = new ArrayList<>();
  private String nombre;
  @Enumerated(value = EnumType.STRING)
  @Column(name = "tipo")
  private TipoDeEntidad tipoDeEntidad;

  public Entidad(String nombre, TipoDeEntidad tipoDeEntidad) {
    this.nombre = nombre;
    this.tipoDeEntidad = tipoDeEntidad;
  }

  public Entidad(){}

  public String getNombre() {
    return nombre;
  }

  public void agregarEstablecimiento(Establecimiento establecimiento) {
    establecimientos.add(establecimiento);
  }

  public void agregarUsuarioInteresado(Usuario usuario) {
    usuariosInteresados.add(usuario);
  }

  public void reportarIncidente(Servicio servicio, LocalDateTime fecha, String observaciones) {
    if (this.tieneServicio(servicio)) {
      Incidente incidente = new Incidente(servicio, observaciones, fecha);
      agregarIncidente(incidente);
      notificarReporteDeIncidente(incidente);
    } else {
      throw new RuntimeException("El servicio debe pertenecer a algún establecimiento de la entidad");
    }
  }

  public boolean tieneServicio(Servicio servicio) {
    return establecimientos.stream()
        .anyMatch(e -> e.tieneServicio(servicio));
  }

  private void agregarIncidente(Incidente incidente) {
    incidentes.add(incidente);
  }

  public void notificarReporteDeIncidente(Incidente incidente) {
    usuariosInteresados.forEach(m -> m.notificarReporteDeIncidente(incidente));
  }

  public List<Incidente> getIncidentesResueltos() {
    return incidentes
        .stream()
        .filter(Incidente::estaResuelto)
        .toList();
  }

  public List<Incidente> getIncidentesAbiertos() {
    return incidentes
        .stream()
        .filter(i -> !i.estaResuelto())
        .toList();
  }
}
