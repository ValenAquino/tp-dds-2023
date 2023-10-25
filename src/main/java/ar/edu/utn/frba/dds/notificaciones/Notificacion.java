package ar.edu.utn.frba.dds.notificaciones;

import ar.edu.utn.frba.dds.entidades.PersistentEntity;
import ar.edu.utn.frba.dds.entidades.Usuario;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 255)
public abstract class Notificacion extends PersistentEntity {
  @ManyToOne(cascade = CascadeType.PERSIST)
  private Usuario receptor;
  private LocalDateTime fecha;
  @Column(name = "fecha_envio", nullable = true)
  private LocalDateTime fechaEnvio;

  public Notificacion(Usuario receptor) {
    this.receptor = receptor;
    this.fecha = LocalDateTime.now();
  }

  public Notificacion() { }

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
  }

  public void enviar() {
    receptor.notificar(this);
  }
}
