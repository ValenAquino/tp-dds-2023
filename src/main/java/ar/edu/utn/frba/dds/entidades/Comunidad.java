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
    if (serviciosDeInteres.contains(servicio)) {
      Incidente incidente = new Incidente(servicio, observaciones);
      incidentes.add(incidente);
      notificarAperturaDeIncidente(incidente);
      return incidente;
    } else {
      throw new RuntimeException("El servicio debe ser de interés para abrir un incidente");
    }
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
    miembros.stream()
        .filter(m -> m.esInteresadoEn(incidente.getServicio()))
        .forEach(m -> m.notificarAperturaDeIncidente(incidente));
  }
}