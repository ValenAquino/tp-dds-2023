package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.notificaciones.horarios.CalendarioNotificaciones;

public class Usuario {
  private String usuario;
  private String contrasenia;
  private String nombre;
  private String apellido;
  private String correoElectronico;
  private MedioDeComunicacion medioDeComunicacion;
  private CalendarioNotificaciones calendarioNotificaciones;

  public Usuario(String usuario, String contrasenia, String nombre, String apellido,
                 String correoElectronico) {
    this.usuario = usuario;
    this.contrasenia = contrasenia;
    this.nombre = nombre;
    this.apellido = apellido;
    this.correoElectronico = correoElectronico;
  }

  public void setMedioDeComunicacion(MedioDeComunicacion medioDeComunicacion) {
    this.medioDeComunicacion = medioDeComunicacion;
  }

  public void setCalendarioNotificaciones(CalendarioNotificaciones calendarioNotificaciones) {
    this.calendarioNotificaciones = calendarioNotificaciones;
  }

  public void notificarAperturaDeIncidente(Incidente incidente) {
    if (calendarioNotificaciones == null ||
        calendarioNotificaciones.abarcaA(incidente.getFecha())) {
      medioDeComunicacion.notificarAperturaDeIncidente(incidente);
    }
  }
  public boolean interesadoEnEntidad(Entidad entidad){
    return entidadesDeInteres.contains(entidad);
  }
}
