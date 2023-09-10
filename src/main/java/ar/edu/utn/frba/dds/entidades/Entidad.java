package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import java.util.ArrayList;
import java.util.List;

public class Entidad {
  private final List<Establecimiento> establecimientos;
  private final List<Usuario> usuariosInteresados;
  private final List<Incidente> incidentes;
  private final String nombre;
  private final TipoDeEntidad tipoDeEntidad;

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

  public void agregarEstablecimiento(Establecimiento establecimiento) {
    establecimientos.add(establecimiento);
  }

  public void agregarUsuarioInteresado(Usuario usuario) {
    usuariosInteresados.add(usuario);
  }

  public Incidente reportarIncidente(Servicio servicio, String observaciones) {
    if (this.tieneServicio(servicio)) {
      Incidente incidente = new Incidente(servicio, observaciones);
      agregarIncidente(incidente);
      notificarReporteDeIncidente(incidente);
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
