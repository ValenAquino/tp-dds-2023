package ar.edu.utn.frba.dds.entidades;

import java.util.List;

public class OrganismoDeControl {
  private String nombre;
  private String correoElectronico;
  private Usuario responsableDeInformes;
  private List<Servicio> servicios;

  public OrganismoDeControl(String nombre, String correoElectronico) {
    this.nombre = nombre;
    this.correoElectronico = correoElectronico;
  }

  public String getNombre() {
    return nombre;
  }

  public void asignarResponsable(Usuario nuevoResponsable) {
    this.responsableDeInformes = nuevoResponsable;
  }
}
