package ar.edu.utn.frba.dds.entidades;

import java.util.ArrayList;
import java.util.List;

public class Entidad {
  private String nombre;
  private TipoDeEntidad tipoDeEntidad;
  private List<Establecimiento> establecimientos;

  public Entidad(String nombre, TipoDeEntidad tipoDeEntidad) {
    this.nombre = nombre;
    this.establecimientos = new ArrayList<>();
    this.tipoDeEntidad = tipoDeEntidad;
  }

  public String getNombre() {
    return nombre;
  }

  public List<Establecimiento> getEstablecimientos() {
    return establecimientos;
  }

  public void agregarEstablecimiento(Establecimiento establecimiento) {
    establecimientos.add(establecimiento);
  }

  public List<Incidente> getIncidentes() {
    List<Incidente> incidentes = new ArrayList<>();
    for (Establecimiento establecimiento : establecimientos) {
      incidentes.addAll(establecimiento.getIncidentes());
    }
    return incidentes;
  }
}
