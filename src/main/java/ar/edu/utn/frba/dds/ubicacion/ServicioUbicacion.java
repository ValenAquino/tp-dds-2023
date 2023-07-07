package ar.edu.utn.frba.dds.ubicacion;

import ar.edu.utn.frba.dds.entidades.Ubicacion;

public interface ServicioUbicacion {
  Ubicacion ubicacionActual(String email);
}
