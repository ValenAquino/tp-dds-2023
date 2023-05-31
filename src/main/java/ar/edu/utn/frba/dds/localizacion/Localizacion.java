package ar.edu.utn.frba.dds.localizacion;

public class Localizacion {
  private String nombre;

  public Localizacion(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return this.nombre;
  }
  public boolean esMismaLocalizacion(Localizacion localizacion){
    return localizacion.nombre == this.nombre;
  }
}
