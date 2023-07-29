package ar.edu.utn.frba.dds.notificaciones;

import ar.edu.utn.frba.dds.entidades.Incidente;

public interface MedioDeComunicacion {
  void notificarAperturaDeIncidente(Incidente incidente, String destinatario);

  void sugerirRevisionDeIncidente(Incidente incidente, String destinatario);
}
