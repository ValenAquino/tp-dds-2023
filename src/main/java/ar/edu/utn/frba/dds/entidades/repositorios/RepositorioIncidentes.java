package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.entidades.Incidente;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioIncidentes {
  private static RepositorioIncidentes instance;
  private List<Incidente> incidentes;

  public List<Incidente> todos() {
    return this.incidentes;
  }

  public static RepositorioIncidentes getInstance() {
    if (instance == null) {
      instance = new RepositorioIncidentes();
    }
    return instance;
  }

  public List<Incidente> ultimaSemana() {
    LocalDateTime ahora = LocalDateTime.now();
    LocalDateTime fechaLimite = ahora.minusDays(7);

    return todos().stream()
        .filter(i -> i.getFecha().isAfter(fechaLimite) && i.getFecha().isBefore(ahora))
        .collect(Collectors.toList());
  }
}
