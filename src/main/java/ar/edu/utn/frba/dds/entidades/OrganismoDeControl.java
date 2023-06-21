package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.excepciones.OrganismoDeControlException;
import java.util.List;

public class OrganismoDeControl {
  private String nombre;
  private String correoElectronico;
  private Usuario responsableDeInformes;
  private List<Entidad> entidadesControladas;

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
    this.responsableDeInformes = nuevoResponsable;
  }

  public void agregarEntidad(Entidad nuevaEntidad) {
    entidadesControladas.add(nuevaEntidad);
  }
}
