package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.notificaciones.horarios.CalendarioNotificaciones;
import ar.edu.utn.frba.dds.ubicacion.ServicioUbicacion;
import java.time.LocalDateTime;

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

  public Ubicacion getUbicacionActual(ServicioUbicacion servicioUbicacion) {
    return servicioUbicacion.ubicacionActual(correoElectronico);
  }

  public String getCorreoElectronico() {
    return correoElectronico;
  }

  public void notificarAperturaDeIncidente(Incidente incidente) {
    if (puedeRecibirNotificacion()) {
      medioDeComunicacion.notificarAperturaDeIncidente(incidente);
    }
  }

  public void sugerirRevisionDeIncidente(Incidente incidente) {
    if (puedeRecibirNotificacion()) {
      medioDeComunicacion.sugerirRevisionDeIncidente(incidente);
    }
  }

  private boolean puedeRecibirNotificacion() {
    return calendarioNotificaciones == null
        || calendarioNotificaciones.abarcaA(LocalDateTime.now());
  }
}
