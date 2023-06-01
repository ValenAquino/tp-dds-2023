package ar.edu.utn.frba.dds.entidades;

import java.util.List;

public class Establecimiento {
  private String nombre;
  private List<Servicio> servicios;

  public void agregarServicio(Servicio servicio) {
    this.servicios.add(servicio);
  }

  public void removerServicio(Servicio servicio) {
    this.servicios.remove(servicio);
  }
}
