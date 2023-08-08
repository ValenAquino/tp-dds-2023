package ar.edu.utn.frba.dds.notificaciones;

import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Usuario;

public interface MedioDeComunicacion {
  void notificarAperturaDeIncidente(Incidente incidente, Usuario destinatario);

  void sugerirRevisionDeIncidente(Incidente incidente, Usuario destinatario);
}
