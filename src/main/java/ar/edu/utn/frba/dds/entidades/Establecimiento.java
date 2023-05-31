package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.localizacion.Localizacion;
import java.util.List;

public class Establecimiento {
  private String nombre;
  private Localizacion localizacion;
  private List<Servicio> servicios;

  public void agregarServicio(Servicio servicio){
    this.servicios.add(servicio);
  }
  public void removerServicio(Servicio servicio){
    this.servicios.remove(servicio);
  }
}
