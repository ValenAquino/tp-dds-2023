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
  public Incidente abrirIncidente(Servicio servicio, String observaciones) {
    if(establecimientos.stream().anyMatch(establecimiento -> establecimiento.tieneServicio(servicio))){
      Incidente incidente = new Incidente(servicio, observaciones);
      servicio.agregarIncidente(incidente);
      RepositorioUsuarios.getInstance().todos().forEach(usuario -> {
        if(usuario.interesadoEnEntidad(this)){
          usuario.notificarAperturaDeIncidente(incidente);
        }
      });
      return incidente;
    }else {
      throw new RuntimeException("El servicio debe pertenecer a algun establecimiento de la entidad");
    }
  }
  public List<Incidente> getIncidentes() {
    List<Incidente> incidentes = new ArrayList<>();
    for (Establecimiento establecimiento : establecimientos) {
      incidentes.addAll(establecimiento.getIncidentes());
    }
    return incidentes;
  }
}
