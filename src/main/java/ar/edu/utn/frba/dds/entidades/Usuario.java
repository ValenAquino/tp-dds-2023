package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
  private String usuario;
  private String contrasenia;
  private String nombre;
  private String apellido;
  private String correoElectronico;
  private List<Servicio> serviciosDeInteres;
  private MedioDeComunicacion medioDeComunicacion;

  public Usuario(String usuario, String contrasenia, String nombre, String apellido,
                 String correoElectronico) {
    this.usuario = usuario;
    this.contrasenia = contrasenia;
    this.nombre = nombre;
    this.apellido = apellido;
    this.correoElectronico = correoElectronico;
    this.serviciosDeInteres = new ArrayList<>();
  }

  public void setMedioDeComunicacion(MedioDeComunicacion medioDeComunicacion) {
    this.medioDeComunicacion = medioDeComunicacion;
  }

  public void agregarServicioDeInteres(Servicio servicio) {
    serviciosDeInteres.add(servicio);
  }
}
