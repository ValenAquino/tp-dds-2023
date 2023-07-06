package ar.edu.utn.frba.dds.entidades;

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

  public Incidente abrirIncidente(Servicio servicio, String observaciones) {
    if (this.leInteresaServicio(servicio)) {
      Incidente incidente = new Incidente(servicio, observaciones);
      agregarIncidente(incidente);
      notificarAperturaDeIncidente(incidente);
      return incidente;
    } else {
      throw new RuntimeException("El servicio debe ser de interés para abrir un incidente");
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

  public void notificarAperturaDeIncidente(Incidente incidente) {
    miembros.forEach(m -> m.notificarAperturaDeIncidente(incidente));
  }
}
