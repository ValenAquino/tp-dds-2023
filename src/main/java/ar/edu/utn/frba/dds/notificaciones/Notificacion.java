package ar.edu.utn.frba.dds.notificaciones;

import ar.edu.utn.frba.dds.entidades.Usuario;
import java.time.LocalDateTime;

public abstract class Notificacion {
  private Usuario receptor;
  private LocalDateTime fechaEnvio;
  private LocalDateTime fecha;

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
  }

  public void enviar() {
    receptor.notificar(this);
  }
}
