package ar.edu.utn.frba.dds.localizacion;

public class Departamento extends Localizacion {
  private Provincia provincia;

  public Departamento(String nombre, Provincia provincia) {
    super(nombre);
    this.provincia = provincia;
  }

  public String getProvinciaNombre() {
    return provincia.getNombre();
  }
}
