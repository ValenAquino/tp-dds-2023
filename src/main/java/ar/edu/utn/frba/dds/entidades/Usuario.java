package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.notificaciones.horarios.CalendarioNotificaciones;
import ar.edu.utn.frba.dds.ubicacion.ServicioUbicacion;
import java.time.LocalDateTime;
import java.util.List;

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

  public void reportarIncidente(Servicio servicio, String observaciones) {
    var comunidades = getComunidadesInteresadas(servicio);

    var incidente = new Incidente(servicio, observaciones);

    for (var comunidad : comunidades) {
      comunidad.reportarIncidente(incidente.copiar());
    }
  }

  public void cerrarIncidente(Comunidad comunidad, Incidente incidente) {
    if (comunidad.tieneIncidente(incidente)) {
      comunidad.cerrarIncidente(incidente);
    } else {
      throw new RuntimeException("El incidente a cerrar debe estar abierto para la comunidad");
    }
  }

  public void notificarReporteDeIncidente(Incidente incidente) {
    if (puedeRecibirNotificacion()) {
      medioDeComunicacion.notificarReporteDeIncidente(incidente, this);
    }
    // TODO: si no puede recibir la notificación ahora,
    //  almacenarla para luego recibir un resumen de todas las notificaciones pendientes
  }

  public void sugerirRevisionDeIncidente(Incidente incidente) {
    if (puedeRecibirNotificacion()) {
      medioDeComunicacion.sugerirRevisionDeIncidente(incidente, this);
    }
  }

  private boolean puedeRecibirNotificacion() {
    return calendarioNotificaciones == null
        || calendarioNotificaciones.abarcaA(LocalDateTime.now());
  }

  private List<Comunidad> getComunidadesInteresadas(Servicio servicio) {
    return RepositorioComunidades
        .getInstance().getComunidadesInteresadas(this, servicio);
  }
}
