package ar.edu.utn.frba.dds.entidades;

public enum TipoDeLocalizacion {
  DEPARTAMENTO("Departamento"),
  MUNICIPIO("Municipio"),
  PROVINCIA("Provincia");

  private final String displayName;

  TipoDeLocalizacion(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return this.displayName;
  }
}