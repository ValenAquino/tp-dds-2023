package ar.edu.utn.frba.dds.entidades;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
  private String usuario;
  private String contrasenia;
  private String nombre;
  private String apellido;
  private String correoElectronico;
  private List<Entidad> entidadesDeInteres;
  private Localizacion localizacion;

  public Usuario(String usuario, String contrasenia, String nombre, String apellido, String correoElectronico, Localizacion localizacion){
    this.usuario = usuario;
    this.contrasenia = contrasenia;
    this.nombre = nombre;
    this.apellido = apellido;
    this.correoElectronico = correoElectronico;
    this.localizacion = localizacion;
    this.entidadesDeInteres = new ArrayList<>();
  }

}
