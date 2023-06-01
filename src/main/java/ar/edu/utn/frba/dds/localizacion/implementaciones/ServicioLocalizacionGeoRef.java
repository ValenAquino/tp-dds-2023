package ar.edu.utn.frba.dds.localizacion.implementaciones;

import ar.edu.utn.frba.dds.entidades.Localizacion;
import ar.edu.utn.frba.dds.localizacion.ServicioLocalizacion;
import ar.edu.utn.frba.dds.entidades.TipoDeLocalizacion;
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

  private Localizacion getLocalizacion(Map<String, Object> localizacionObj, TipoDeLocalizacion type) {
    return new Localizacion(
        (String) localizacionObj.get("nombre"),
        (double) localizacionObj.get("centroide_lat"),
        (double) localizacionObj.get("centroide_lon"),
        type
    );
  }
}
