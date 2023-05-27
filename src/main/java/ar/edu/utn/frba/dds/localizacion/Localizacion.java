package ar.edu.utn.frba.dds.localizacion;

public abstract class Localizacion {
  String nombre;

  public Localizacion(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return this.nombre;
  }
}
