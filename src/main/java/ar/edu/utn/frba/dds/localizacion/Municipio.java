package ar.edu.utn.frba.dds.localizacion;

public class Municipio extends Localizacion {
  private Provincia provincia;

  public Municipio(String nombre, Provincia provincia) {
    super(nombre);
    this.provincia = provincia;
  }

  public String getProvinciaNombre() {
    return provincia.getNombre();
  }
}
