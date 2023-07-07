package ar.edu.utn.frba.dds.entidades;

import java.util.ArrayList;
import java.util.List;

public class OrganismoDeControl {
  private final String nombre;
  private final String correoElectronico;
  private Usuario usuarioResponsable;
  private final List<Entidad> entidadesControladas = new ArrayList<>();

  public OrganismoDeControl(String nombre, String correoElectronico) {
    this.nombre = nombre;
    this.correoElectronico = correoElectronico;
  }

  public String getNombre() {
    return nombre;
  }

  public String getCorreoElectronico() {
    return correoElectronico;
  }

  public void asignarResponsable(Usuario nuevoResponsable) {
    usuarioResponsable = nuevoResponsable;
  }

  public void agregarEntidad(Entidad nuevaEntidad) {
    entidadesControladas.add(nuevaEntidad);
  }

  public List<Entidad> getEntidades() {
    return entidadesControladas;
  }
}
