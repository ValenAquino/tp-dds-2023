package ar.edu.utn.frba.dds.notificaciones;

import ar.edu.utn.frba.dds.entidades.PersistentEntity;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioNotificaciones;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "notificaciones")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 255)
public abstract class Notificacion extends PersistentEntity {
  @ManyToOne
  private final Usuario receptor;
  private final LocalDateTime fecha;
  @Column(name = "fecha_envio", nullable = true)
  private LocalDateTime fechaEnvio;

  public Notificacion(Usuario receptor) {
    this.receptor = receptor;
    this.fecha = LocalDateTime.now();
  }

  public abstract String getAsunto();

  public abstract String getMensaje();

  public Usuario getReceptor() {
    return this.receptor;
  }

  public boolean fueEnviada() {
    return this.fechaEnvio != null;
  }

  public void marcarComoEnviada() {
    this.fechaEnvio = LocalDateTime.now();
    // TODO: Chequear si para update seria algo distinto
    RepositorioNotificaciones.getInstance().persistir(this);
  }

  public void enviar() {
    receptor.notificar(this);
    RepositorioNotificaciones.getInstance().persistir(this);
  }
}
