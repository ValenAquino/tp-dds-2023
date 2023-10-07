package ar.edu.utn.frba.dds.notificaciones;

import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Usuario;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("RevisiónIncidente")
public class NotificacionRevisionIncidente extends Notificacion {
  @ManyToOne
  Incidente incidente;
  public NotificacionRevisionIncidente(Usuario receptor, Incidente incidente) {
    super(receptor);
    this.incidente = incidente;
  }

  public NotificacionRevisionIncidente() { }

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
