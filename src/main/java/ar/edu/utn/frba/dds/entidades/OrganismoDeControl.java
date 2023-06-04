package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.excepciones.OrganismoDeControlException;
import java.util.List;

public class OrganismoDeControl {
  private String nombre;
  private String correoElectronico;
  private Usuario responsableDeInformes;
  private List<Entidad> entidadesControladas;

  public OrganismoDeControl(String nombre, String correoElectronico) {
    // TODO: Validar que los campos sean válidos
    validarCampos(nombre, correoElectronico);
    this.nombre = nombre;
    this.correoElectronico = correoElectronico;
  }

  private void validarCampos(String nombre, String correoElectronico) {
    if (nombre == null || nombre.isEmpty()) {
      throw new OrganismoDeControlException("El nombre no puede ser nulo o vacio");
    }
    if (correoElectronico == null || correoElectronico.isEmpty()) {
      throw new OrganismoDeControlException("El correo electronico no puede ser nulo o vacio");
    }
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
