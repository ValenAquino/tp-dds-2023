package ar.edu.utn.frba.dds.entidades;

import java.util.ArrayList;
import java.util.List;

public class Entidad {
  private String nombre;
  private TipoDeEntidad tipoDeEntidad;
  private List<Establecimiento> establecimientos;

  public Entidad(String nombre) {
    this.nombre = nombre;
    this.establecimientos = new ArrayList<>();
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
}
