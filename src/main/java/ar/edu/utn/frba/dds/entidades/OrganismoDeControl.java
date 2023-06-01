package ar.edu.utn.frba.dds.entidades;

import java.util.List;

public class OrganismoDeControl {
  private String nombre;
  private String correoElectronico;
  private Usuario responsableDeInformes;
  private List<Servicio> servicios;

  public OrganismoDeControl(String nombre, String correoElectronico, Usuario responsableDeInformes) {
    this.nombre = nombre;
    this.correoElectronico = correoElectronico;
    this.responsableDeInformes = responsableDeInformes;
  }

  public void asignarResponsable(Usuario nuevoResponsable) {
    this.responsableDeInformes = nuevoResponsable;
  }
}
