package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.notificaciones.Notificacion;
import ar.edu.utn.frba.dds.notificaciones.NotificacionNuevoIncidente;
import ar.edu.utn.frba.dds.notificaciones.NotificacionRevisionIncidente;
import ar.edu.utn.frba.dds.notificaciones.horarios.CalendarioNotificaciones;
import ar.edu.utn.frba.dds.ubicacion.ServicioMapas;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario extends PersistentEntity {
  private final String usuario;
  private final String contrasenia;
  private final String nombre;
  private final String apellido;
  @Column(name = "correo_electronico")
  private final String correoElectronico;

  @ManyToOne
  @JoinColumn(name = "medio_de_comunicacion_id")
  private MedioDeComunicacion medioDeComunicacion;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "calendario_notificaciones_id")
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

  public Ubicacion getUbicacionActual(ServicioMapas servicioMapas) {
    return servicioMapas.ubicacionActual(correoElectronico);
  }

  public String getCorreoElectronico() {
    return correoElectronico;
  }

  public void reportarIncidente(Servicio servicio, String observaciones) {
    var ahora = LocalDateTime.now();
    getComunidadesInteresadas(servicio).forEach(c ->
        c.reportarIncidente(servicio, observaciones, ahora, this)
    );
  }

  public void cerrarIncidente(Comunidad comunidad, Incidente incidente) {
    if (comunidad.tieneIncidente(incidente)) {
      comunidad.cerrarIncidente(incidente);
    } else {
      throw new RuntimeException("El incidente a cerrar debe estar abierto para la comunidad");
    }
  }

  public void notificarReporteDeIncidente(Incidente incidente) {
    NotificacionNuevoIncidente notificacion = new NotificacionNuevoIncidente(this, incidente);
    this.notificar(notificacion);
  }

  public void sugerirRevisionDeIncidente(Incidente incidente) {
    NotificacionRevisionIncidente notificacion = new NotificacionRevisionIncidente(this, incidente);
    this.notificar(notificacion);
  }

  public void notificar(Notificacion notificacion) {
    if (puedeRecibirNotificacion()) {
      medioDeComunicacion.notificar(notificacion);
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
