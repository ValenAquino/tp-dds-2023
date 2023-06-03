package ar.edu.utn.frba.dds.entidades;

import java.util.ArrayList;
import java.util.List;

public class Entidad {
  private String nombre;
  private Localizacion localizacion;
  private TipoDeEntidad tipoDeEntidad;
  private List<Establecimiento> establecimientos;

  public Entidad(String nombre, Localizacion localizacion) {
    this.localizacion = localizacion;
    this.nombre = nombre;
    this.establecimientos = new ArrayList<>();
  }

  public String getNombre() {
    return nombre;
  }

  public Localizacion getLocalizacion() {
    return localizacion;
  }

  public List<Establecimiento> getEstablecimientos() {
    return establecimientos;
  }

  public void agregarEstablecimiento(Establecimiento establecimiento) {
    establecimientos.add(establecimiento);
  }
}
