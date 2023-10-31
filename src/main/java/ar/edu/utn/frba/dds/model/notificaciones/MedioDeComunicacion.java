package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.model.entidades.PersistentEntity;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "medios_de_comunicacion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "descripcion", length = 255)
public abstract class MedioDeComunicacion extends PersistentEntity {
  public final void notificar(Notificacion notificacion) {
    procesarNotificacion(notificacion);
    notificacion.marcarComoEnviada();
  }

  public abstract void procesarNotificacion(Notificacion notificacion);
}
