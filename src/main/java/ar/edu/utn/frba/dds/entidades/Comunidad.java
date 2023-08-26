package ar.edu.utn.frba.dds.entidades;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Comunidad {
  List<Servicio> serviciosDeInteres;
  List<Incidente> incidentes;
  List<Usuario> miembros;

  public Comunidad() {
    this.serviciosDeInteres = new ArrayList<>();
    this.incidentes = new ArrayList<>();
    this.miembros = new ArrayList<>();
  }

  public List<Incidente> getIncidentes() {
    return incidentes;
  }

  public void agregarServicioDeInteres(Servicio servicio) {
    serviciosDeInteres.add(servicio);
  }

  public void agregarMiembro(Usuario usuario) {
    miembros.add(usuario);
  }

  public Incidente reportarIncidente(Servicio servicio, String observaciones,
                                     LocalDateTime ahora, Usuario reportante) {
    var incidente = new Incidente(servicio, observaciones, ahora, reportante);
    agregarIncidente(incidente);
    notificarReporteDeIncidente(incidente);
    return incidente;
  }

  public void cerrarIncidente(Incidente incidente) {
    if (!incidente.estaResuelto()) {
      incidente.cerrar();
    }
  }

  public boolean leInteresaServicio(Servicio servicio) {
    return serviciosDeInteres.contains(servicio);
  }

  private void agregarIncidente(Incidente incidente) {
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

  public boolean tieneMiembro(Usuario usuario) {
    return miembros.contains(usuario);
  }

  public boolean tieneIncidente(Incidente incidente) {
    return incidentes.contains(incidente);
  }

  public List<Usuario> getMiembrosANotificar(Incidente incidente) {
    return miembros
        .stream()
        .filter(m -> !m.esReportante(incidente))
        .toList();
  }
}
