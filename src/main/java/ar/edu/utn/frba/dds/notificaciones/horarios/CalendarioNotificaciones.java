package ar.edu.utn.frba.dds.notificaciones.horarios;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Map;

public class CalendarioNotificaciones {
  private final Map<DayOfWeek, RangoHorario> horarios;

  public CalendarioNotificaciones(Map<DayOfWeek, RangoHorario> horarios) {
    this.horarios = horarios;
  }

  public boolean abarcaA(LocalDateTime fecha) {
    RangoHorario rangoHorario = horarios.get(fecha.getDayOfWeek());
    return rangoHorario != null && rangoHorario.contieneHorario(fecha.toLocalTime());
  }
}
