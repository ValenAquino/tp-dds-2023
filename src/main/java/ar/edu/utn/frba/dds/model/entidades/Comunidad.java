package ar.edu.utn.frba.dds.model.entidades;

import ar.edu.utn.frba.dds.model.ubicacion.ServicioMapas;
import ar.edu.utn.frba.dds.model.ubicacion.implementaciones.ServicioGoogleMaps;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comunidades")
public class Comunidad extends PersistentEntity {
  @ManyToMany
  @JoinTable(
      name = "servicios_interesados_por_comunidades",
      joinColumns = @JoinColumn(name = "comunidad_id"),
      inverseJoinColumns = @JoinColumn(name = "servicio_id"))
  List<Servicio> serviciosDeInteres;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "incidentes_por_comunidades",
      joinColumns = @JoinColumn(name = "comunidad_id"),
      inverseJoinColumns = @JoinColumn(name = "incidente_id"))
  List<Incidente> incidentes;

  @ManyToMany
  @JoinTable(
      name = "miembros_por_comunidades",
      joinColumns = @JoinColumn(name = "comunidad_id"),
      inverseJoinColumns = @JoinColumn(name = "usuario_id"))
  List<Usuario> miembros;

  String nombre;

  @Transient
  ServicioMapas servicioMapas;

  @PostLoad
  public void postLoad() {
    this.servicioMapas = new ServicioGoogleMaps();
  }

  public Comunidad(String nombre, ServicioMapas servicioMapa) {
    this.nombre = nombre;
    this.serviciosDeInteres = new ArrayList<>();
    this.incidentes = new ArrayList<>();
    this.miembros = new ArrayList<>();
    this.servicioMapas = servicioMapa;
  }

  public Comunidad() { }

  public String getNombre() {
    return nombre;
  }
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public List<Incidente> getIncidentes() {
    return incidentes;
  }

  public List<Usuario> getMiembros() {
    return miembros;
  }

  public List<Servicio> getServiciosDeInteres() {
    return serviciosDeInteres;
  }

  public void agregarServicioDeInteres(Servicio servicio) {
    serviciosDeInteres.add(servicio);
  }

  public void eliminarServiciosDeInteres() {
    this.serviciosDeInteres.clear();
  }

  public void agregarMiembro(Usuario usuario) {
    miembros.add(usuario);
  }

  public void eliminarMiembro(Usuario usuario) {
    miembros.remove(usuario);
  }

  public void eliminarMiembros() {
    this.miembros.clear();
  }

  public Incidente reportarIncidente(Servicio servicio, String observaciones,
                                     LocalDateTime ahora, Usuario reportante) {
    var incidente = new Incidente(servicio, observaciones, ahora, reportante);
    agregarIncidente(incidente);
    notificarReporteDeIncidente(incidente);
    return incidente;
  }

  public void cerrarIncidente(Incidente incidente, LocalDateTime fecha) {
    if (!incidente.estaResuelto()) {
      incidente.cerrar(fecha);
    }
  }

  public boolean leInteresaServicio(Servicio servicio) {
    return serviciosDeInteres.contains(servicio);
  }

  public void agregarIncidente(Incidente incidente) {
    incidentes.add(incidente);
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

  public void notificarReporteDeIncidente(Incidente incidente) {
    getMiembrosANotificar(incidente)
        .forEach(m -> m.notificarReporteDeIncidente(incidente));
  }

  public List<Incidente> getIncidentesAbiertosCercanosA(Usuario usuario) {
    return this.getIncidentesAbiertos().stream().filter(i ->
        servicioMapas.estanCerca(
            usuario.getUbicacionActual(servicioMapas),
            i.getUbicacion(),
            200
        )
    ).toList();
  }

  public boolean tieneMiembro(Usuario usuario) {
    return miembros.contains(usuario);
  }

  public boolean tieneIncidente(Incidente incidente) {
    return incidentes.contains(incidente);
  }

  public List<Usuario> getMiembrosANotificar(Incidente incidente) {
    return miembros
        .stream()
        .filter(m -> incidente.getReportante() != m)
        .toList();
  }

  public int getId() { return id; }
}
