package ar.edu.utn.frba.dds.notificaciones;

import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Usuario;
import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@DiscriminatorValue("NuevoIncidente")
public class NotificacionNuevoIncidente extends Notificacion {

  @ManyToOne
  private final Incidente incidente;

  public NotificacionNuevoIncidente(Usuario receptor, Incidente incidente) {
    super(receptor);
    this.incidente = incidente;
  }

  @Override
  public String getAsunto() {
    return "¡Nuevo incidente!";
  }

  @Override
  public String getMensaje() {
    return String.format(
        "Se ha abierto un nuevo incidente en el servicio %s a las %s",
        incidente.getServicio().getDescripcion(),
        incidente.getFecha()
    );
  }

  public Incidente getIncidente() {
    return incidente;
  }
}
