package ar.edu.utn.frba.dds.localizacion.implementaciones;

import ar.edu.utn.frba.dds.entidades.Localizacion;
import ar.edu.utn.frba.dds.entidades.Ubicacion;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeLocalizacion;
import ar.edu.utn.frba.dds.localizacion.ServicioLocalizacion;
import ar.edu.utn.frba.dds.localizacion.apis.GeoRefApiCliente;
import java.util.Map;

public class ServicioLocalizacionGeoRef implements ServicioLocalizacion {
  private final GeoRefApiCliente apiCliente;

  public ServicioLocalizacionGeoRef(GeoRefApiCliente apiCliente) {
    this.apiCliente = apiCliente;
  }

  public Localizacion getProvincia(String nombre) {
    return getLocalizacion(
        this.apiCliente.getProvinciaFromApi(nombre),
        TipoDeLocalizacion.PROVINCIA
    );
  }

  public Localizacion getDepartamento(String nombre, String provinciaNombre) {
    return getLocalizacion(
        this.apiCliente.getDepartamentoFromApi(nombre, provinciaNombre),
        TipoDeLocalizacion.DEPARTAMENTO
    );
  }

  public Localizacion getMunicipio(String nombre, String provinciaNombre) {
    return getLocalizacion(
        this.apiCliente.getMunicipioFromApi(nombre, provinciaNombre),
        TipoDeLocalizacion.MUNICIPIO
    );
  }

  private Localizacion getLocalizacion(Map<String, Object> respuestaApi, TipoDeLocalizacion type) {
    return new Localizacion(
        (String) respuestaApi.get("nombre"),
        new Ubicacion(
            (double) respuestaApi.get("centroide_lat"),
            (double) respuestaApi.get("centroide_lon")
        ),
        type
    );
  }
}
