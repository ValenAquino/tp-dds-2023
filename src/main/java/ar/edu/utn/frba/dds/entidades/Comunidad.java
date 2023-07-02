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

  public Incidente abrirIncidente(Servicio servicio, String observaciones) {
    if (serviciosDeInteres.contains(servicio)) {
      Incidente incidente = new Incidente(servicio, observaciones);
      incidentes.add(incidente);
      return incidente;
    } else {
      throw new RuntimeException("El servicio debe ser de interés para abrir un incidente");
    }
  }
}
