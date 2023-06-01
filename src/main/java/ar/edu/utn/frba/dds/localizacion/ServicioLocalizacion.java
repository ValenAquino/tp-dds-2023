package ar.edu.utn.frba.dds.localizacion;

import ar.edu.utn.frba.dds.entidades.Localizacion;

public interface ServicioLocalizacion {
  Localizacion getProvincia(String nombre);

  Localizacion getMunicipio(String nombre, String provinciaNombre);

  Localizacion getDepartamento(String nombre, String provinciaNombre);
}
