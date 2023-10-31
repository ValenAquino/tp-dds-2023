package ar.edu.utn.frba.dds.model.ubicacion;

import ar.edu.utn.frba.dds.model.entidades.Ubicacion;

public interface ServicioMapas {
  boolean estanCerca(Ubicacion ubicacion, Ubicacion otraUbicacion, long maxDistanciaEnMetros);

  Ubicacion ubicacionActual(String email);
}
