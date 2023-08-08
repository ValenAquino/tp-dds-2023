package ar.edu.utn.frba.dds.notificaciones.medios;

import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;

public class WhatsAppSender implements MedioDeComunicacion {
  @Override
  public void notificarAperturaDeIncidente(Incidente incidente, Usuario destinatario) {

  }

  @Override
  public void sugerirRevisionDeIncidente(Incidente incidente, Usuario destinatario) {

  }
}
