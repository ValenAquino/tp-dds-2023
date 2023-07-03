package ar.edu.utn.frba.dds.notificaciones.horarios;

import java.time.LocalTime;

public class RangoHorario {
  private final LocalTime inicio;
  private final LocalTime fin;

  public RangoHorario(LocalTime inicio, LocalTime fin) {
    this.inicio = inicio;
    this.fin = fin;
  }

  public boolean contieneHorario(LocalTime horarioAComparar) {
    return horarioAComparar.isAfter(inicio) && horarioAComparar.isBefore(fin);
  }
}