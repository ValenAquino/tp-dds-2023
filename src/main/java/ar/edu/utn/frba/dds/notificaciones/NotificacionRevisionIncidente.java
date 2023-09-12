package ar.edu.utn.frba.dds.notificaciones;

import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Usuario;
import org.hibernate.annotations.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class NotificacionRevisionIncidente extends Notificacion {
  @ManyToOne
  @JoinColumn(name = "incidente_id")
  Incidente incidente;
  public NotificacionRevisionIncidente(Usuario receptor, Incidente incidente) {
    super(receptor);
    this.incidente = incidente;
  }

  @Override
  public String getAsunto() {
    return "Sugerencia de revisión de incidente";
  }

  @Override
  public String getMensaje() {
    return String.format(
        "Le sugerimos revisar el servicio %s con incidente: %s",
        incidente.getServicio().getDescripcion(),
        incidente.getObservaciones()
    );
  }
  public Incidente getIncidente() {
    return incidente;
  }
}
