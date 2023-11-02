package ar.edu.utn.frba.dds.model.entidades;

import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioNotificaciones;
import ar.edu.utn.frba.dds.model.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.model.notificaciones.Notificacion;
import ar.edu.utn.frba.dds.model.notificaciones.NotificacionNuevoIncidente;
import ar.edu.utn.frba.dds.model.notificaciones.NotificacionRevisionIncidente;
import ar.edu.utn.frba.dds.model.notificaciones.horarios.CalendarioNotificaciones;
import ar.edu.utn.frba.dds.model.ubicacion.ServicioMapas;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario extends PersistentEntity {
  private String usuario;
  private String contrasenia;
  private String nombre;
  private String apellido;
  @Column(name = "es_admin")
  private boolean esAdmin;
  @Column(name = "correo_electronico")
  private String correoElectronico;

  @ManyToOne
  @JoinColumn(name = "medio_de_comunicacion_id")
  private MedioDeComunicacion medioDeComunicacion;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "calendario_notificaciones_id")
  private CalendarioNotificaciones calendarioNotificaciones;

  @Transient
  private RepositorioComunidades repositorioComunidades;

  @Transient
  private RepositorioNotificaciones repositorioNotificaciones;

  public Usuario() { }

  @PostLoad
  public void postLoad() {
    this.repositorioComunidades = RepositorioComunidades.getInstance();
    this.repositorioNotificaciones = RepositorioNotificaciones.getInstance();
  }

  public Usuario(String usuario, String contrasenia, String nombre, String apellido,
                 String correoElectronico) {
    this.usuario = usuario;
    this.contrasenia = contrasenia;
    this.nombre = nombre;
    this.apellido = apellido;
    this.correoElectronico = correoElectronico;
    this.repositorioComunidades = RepositorioComunidades.getInstance();
    this.repositorioNotificaciones = RepositorioNotificaciones.getInstance();
  }

  public Usuario(String usuario, String contrasenia, String nombre, String apellido,
                 String correoElectronico, RepositorioComunidades repositorioComunidades,
                 RepositorioNotificaciones repositorioNotificaciones) {
    this(usuario, contrasenia, nombre, apellido, correoElectronico);
    this.repositorioComunidades = repositorioComunidades;
    this.repositorioNotificaciones = repositorioNotificaciones;
  }

  public boolean esAdmin() {
    return esAdmin;
  }

  public void setAdmin(boolean esAdmin) {
    this.esAdmin = esAdmin;
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

  public String getUsuario() { return usuario; }

  public void reportarIncidente(Servicio servicio, LocalDateTime fecha, String observaciones) {
    getComunidadesInteresadas(servicio).forEach(c ->
        c.reportarIncidente(servicio, observaciones, fecha, this)
    );
  }

  public void cerrarIncidente(Comunidad comunidad, Incidente incidente, LocalDateTime fecha) {
    if (comunidad.tieneIncidente(incidente)) {
      comunidad.cerrarIncidente(incidente, fecha);
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
    repositorioNotificaciones.persistir(notificacion);
    if (puedeRecibirNotificacion()) {
      medioDeComunicacion.notificar(notificacion);
    }
  }

  private boolean puedeRecibirNotificacion() {
    return calendarioNotificaciones == null
        || calendarioNotificaciones.abarcaA(LocalDateTime.now());
  }

  private List<Comunidad> getComunidadesInteresadas(Servicio servicio) {
    return repositorioComunidades.comunidadesInteresadas(this, servicio);
  }
}
