package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.entidades.enums.TipoDeLocalizacion;

public class Localizacion {
  private String nombre;
  private Ubicacion ubicacion;
  private TipoDeLocalizacion tipo;

  public Localizacion(String nombre, Ubicacion ubicacion, TipoDeLocalizacion tipo) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.tipo = tipo;
  }

  public String getNombre() {
    return this.nombre;
  }

  public TipoDeLocalizacion getTipo() {
    return this.tipo;
  }
}
