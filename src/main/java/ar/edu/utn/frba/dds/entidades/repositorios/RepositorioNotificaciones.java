package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.notificaciones.Notificacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioNotificaciones implements WithSimplePersistenceUnit {
  private static RepositorioNotificaciones instance;
  private final List<Notificacion> notificaciones = new ArrayList<>();
  @SuppressWarnings("unchecked")
  public List<Notificacion> todas() {
    return entityManager().createQuery("from Notificacion").getResultList();
  }
  @SuppressWarnings("unchecked")
  public List<Notificacion> notificacionesPendientes() {
    return todas().stream()
        .filter(n -> !n.fueEnviada())
        .collect(Collectors.toList());

    // Con un boolean enviada la consulta podría ser
    // return entityManager()
    //        .createQuery("from Notificacion n where n.enviada = false")
    //        .getResultList();
  }
}