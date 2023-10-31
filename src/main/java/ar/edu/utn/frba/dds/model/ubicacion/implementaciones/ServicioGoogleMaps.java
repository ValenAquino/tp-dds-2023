package ar.edu.utn.frba.dds.model.ubicacion.implementaciones;

import ar.edu.utn.frba.dds.model.entidades.Ubicacion;
import ar.edu.utn.frba.dds.model.ubicacion.ServicioMapas;

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
