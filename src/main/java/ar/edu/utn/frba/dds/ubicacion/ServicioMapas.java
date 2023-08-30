package ar.edu.utn.frba.dds.ubicacion;

import ar.edu.utn.frba.dds.entidades.Ubicacion;

public interface ServicioMapas {
  boolean estanCerca(Ubicacion ubicacion, Ubicacion otraUbicacion, long maxDistanciaEnMetros);
  Ubicacion ubicacionActual(String email);
}
