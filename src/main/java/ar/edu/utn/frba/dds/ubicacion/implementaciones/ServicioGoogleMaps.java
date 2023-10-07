package ar.edu.utn.frba.dds.ubicacion.implementaciones;

import ar.edu.utn.frba.dds.entidades.Ubicacion;
import ar.edu.utn.frba.dds.ubicacion.ServicioMapas;

public class ServicioGoogleMaps implements ServicioMapas {

  @Override
  public boolean estanCerca(Ubicacion ubicacion, Ubicacion otraUbicacion,
                            long maxDistanciaEnMetros) {
    // TODO: implementar
    return false;
  }

  @Override
  public Ubicacion ubicacionActual(String email) {
    // TODO: implementar
    return null;
  }

}
