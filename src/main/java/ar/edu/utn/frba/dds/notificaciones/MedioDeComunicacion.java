package ar.edu.utn.frba.dds.notificaciones;

import ar.edu.utn.frba.dds.entidades.PersistentEntity;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "medio_de_comunicacion")
public abstract class MedioDeComunicacion extends PersistentEntity {
  public final void notificar(Notificacion notificacion) {
    procesarNotificacion(notificacion);
    notificacion.marcarComoEnviada();
  }

  public abstract void procesarNotificacion(Notificacion notificacion);
}
