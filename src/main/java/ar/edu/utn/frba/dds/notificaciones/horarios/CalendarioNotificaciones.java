package ar.edu.utn.frba.dds.notificaciones.horarios;

import ar.edu.utn.frba.dds.entidades.PersistentEntity;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.Table;

@Entity
@Table(name = "calendarios_notificaciones")
public class CalendarioNotificaciones extends PersistentEntity {
  @ElementCollection
  @CollectionTable(name = "horarios_notificaciones", joinColumns = @JoinColumn(name = "calendario_id"))
  @MapKeyColumn(name = "dia_de_semana")
  @MapKeyEnumerated(EnumType.STRING)
  private final Map<DayOfWeek, RangoHorario> horarios;

  public CalendarioNotificaciones(Map<DayOfWeek, RangoHorario> horarios) {
    this.horarios = horarios;
  }

  public boolean abarcaA(LocalDateTime fecha) {
    RangoHorario rangoHorario = horarios.get(fecha.getDayOfWeek());
    return rangoHorario != null && rangoHorario.contieneHorario(fecha.toLocalTime());
  }
}
