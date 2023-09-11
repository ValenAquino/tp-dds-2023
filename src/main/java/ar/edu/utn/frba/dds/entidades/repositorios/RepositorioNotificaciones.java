package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.notificaciones.Notificacion;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioNotificaciones {
  private static RepositorioNotificaciones instance;
  private final List<Notificacion> notificaciones = new ArrayList<>();

  public List<Notificacion> todas() {
    return this.notificaciones;
  }

  public static RepositorioNotificaciones getInstance() {
    if (instance == null) {
      instance = new RepositorioNotificaciones();
    }
    return instance;
  }

  public List<Notificacion> notificacionesPendientes() {
    return todas().stream()
        .filter(n -> !n.fueEnviada())
        .collect(Collectors.toList());
  }
}