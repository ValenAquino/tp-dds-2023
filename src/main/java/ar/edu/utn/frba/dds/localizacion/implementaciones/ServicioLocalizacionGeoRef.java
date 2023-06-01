package ar.edu.utn.frba.dds.localizacion.implementaciones;

import ar.edu.utn.frba.dds.entidades.Localizacion;
import ar.edu.utn.frba.dds.excepciones.LocalizacionNoExistenteException;
import ar.edu.utn.frba.dds.localizacion.ServicioLocalizacion;
import ar.edu.utn.frba.dds.localizacion.TipoDeLocalizacion;
import ar.edu.utn.frba.dds.localizacion.apis.GeoRefApiCliente;
import java.util.ArrayList;
import java.util.HashMap;

public class ServicioLocalizacionGeoRef implements ServicioLocalizacion {
  private GeoRefApiCliente apiCliente;

  public ServicioLocalizacionGeoRef(GeoRefApiCliente apiCliente) {
    this.apiCliente = apiCliente;
  }

  public Localizacion getProvincia(String nombre) {
    var respuesta = this.apiCliente.getProvinciaFromApi(nombre);

    var array = (ArrayList) respuesta.get("provincias");

    if (array.size() == 0) {
      throw new LocalizacionNoExistenteException("Provincia inexistente");
    }

    var provinciaObj = (HashMap) array.get(0);

    var lat = (double) provinciaObj.get("centroide_lat");
    var lon = (double) provinciaObj.get("centroide_lon");

    return new Localizacion(nombre, lat, lon, TipoDeLocalizacion.PROVINCIA);
  }

  public Localizacion getDepartamento(String nombre, String provinciaNombre) {
    var respuesta = this.apiCliente.getDepartamentoFromApi(nombre, provinciaNombre);

    var array = (ArrayList) respuesta.get("departamentos");

    if (array.size() == 0) {
      throw new LocalizacionNoExistenteException("Departamento inexistente");
    }

    var departamentoObj = (HashMap) array.get(0);

    var lat = (double) departamentoObj.get("centroide_lat");
    var lon = (double) departamentoObj.get("centroide_lon");

    return new Localizacion(nombre, lat, lon, TipoDeLocalizacion.DEPARTAMENTO);
  }

  public Localizacion getMunicipio(String nombre, String provinciaNombre) {
    var respuesta = this.apiCliente.getMunicipioFromApi(nombre, provinciaNombre);

    var array = (ArrayList) respuesta.get("municipios");

    if (array.size() == 0) {
      throw new LocalizacionNoExistenteException("Municipio inexistente");
    }

    var municipioObj = (HashMap) array.get(0);

    var lat = (double) municipioObj.get("centroide_lat");
    var lon = (double) municipioObj.get("centroide_lon");

    return new Localizacion(nombre, lat, lon, TipoDeLocalizacion.MUNICIPIO);
  }
}
