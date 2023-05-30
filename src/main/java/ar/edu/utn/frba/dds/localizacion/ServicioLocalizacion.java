package ar.edu.utn.frba.dds.localizacion;

public interface ServicioLocalizacion {
  Provincia getProvincia(String nombre);

  Municipio getMunicipio(String nombre, String provinciaNombre);

  Departamento getDepartamento(String nombre, String provinciaNombre);
}
