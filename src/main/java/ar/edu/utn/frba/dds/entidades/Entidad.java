package ar.edu.utn.frba.dds.entidades;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Entidad {
  private String nombre;
  private TipoDeEntidad tipoDeEntidad;
  private List<Establecimiento> establecimientos;

  public Entidad(String nombre, TipoDeEntidad tipoDeEntidad) {
    this.nombre = nombre;
    this.establecimientos = new ArrayList<>();
    this.tipoDeEntidad = tipoDeEntidad;
  }

  public String getNombre() {
    return nombre;
  }

  public List<Establecimiento> getEstablecimientos() {
    return establecimientos;
  }

  public void agregarEstablecimiento(Establecimiento establecimiento) {
    establecimientos.add(establecimiento);
  }

  public List<Incidente> getIncidentes() {
    return establecimientos.stream()
        .flatMap(establecimiento -> establecimiento.getIncidentes().stream())
        .collect(Collectors.toList());
  }

  public List<Incidente> getIncidentesSemana(LocalDateTime fecha) {
    LocalDateTime fechaLimite = fecha.minusDays(7);

    return establecimientos.stream()
        .flatMap(establecimiento -> establecimiento.getIncidentes().stream())
        .filter(inc -> inc.getFecha().isAfter(fechaLimite) && inc.getFecha().isBefore(fecha))
        .collect(Collectors.toList());
  }


}
