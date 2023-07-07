package ar.edu.utn.frba.dds.entidades;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Entidad {
  private String nombre;
  private TipoDeEntidad tipoDeEntidad;
  private final List<Establecimiento> establecimientos;
  private final List<Usuario> usuariosInteresados;
  private final List<Incidente> incidentes;

  public Entidad(String nombre, TipoDeEntidad tipoDeEntidad) {
    this.nombre = nombre;
    this.tipoDeEntidad = tipoDeEntidad;
    this.establecimientos = new ArrayList<>();
    this.usuariosInteresados = new ArrayList<>();
    this.incidentes = new ArrayList<>();
  }

  public String getNombre() {
    return nombre;
  }

  public List<Establecimiento> getEstablecimientos() {
    return establecimientos;
  }

  public List<Usuario> getUsuariosInteresados() {
    return usuariosInteresados;
  }

  public void agregarEstablecimiento(Establecimiento establecimiento) {
    establecimientos.add(establecimiento);
  }

  public void agregarUsuarioInteresado(Usuario usuario) {
    usuariosInteresados.add(usuario);
  }

  public Incidente abrirIncidente(Servicio servicio, String observaciones) {
    if (this.tieneServicio(servicio)) {
      Incidente incidente = new Incidente(servicio, observaciones);
      agregarIncidente(incidente);
      notificarAperturaDeIncidente(incidente);
      return incidente;
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

  public void notificarAperturaDeIncidente(Incidente incidente) {
    usuariosInteresados.forEach(m -> m.notificarAperturaDeIncidente(incidente));
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

  public List<Incidente> getIncidentes() {
    return incidentes;
  }
}
