package ar.edu.utn.frba.dds.localizacion;

public interface ServicioLocalizacion {
  Localizacion getProvincia(String nombre);

  Localizacion getMunicipio(String nombre, String provinciaNombre);

  Localizacion getDepartamento(String nombre, String provinciaNombre);
}
