package ar.edu.utn.frba.dds.localizacion.apis;

import ar.edu.utn.frba.dds.localizacion.Departamento;
import ar.edu.utn.frba.dds.localizacion.LocalizacionApiException;
import ar.edu.utn.frba.dds.localizacion.Municipio;
import ar.edu.utn.frba.dds.localizacion.Provincia;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class GeoRefApiCliente {
  private Client client = ClientBuilder.newClient();

  public List<Provincia> getProvincias() {
    var provincias = new ArrayList<Provincia>();

    try {
      var respuesta = consultarApi("https://apis.datos.gob.ar/georef/api/provincias");

      var array = (ArrayList) respuesta.get("provincias");

      for (var elemento : array) {
        var nombre = ((HashMap) elemento).get("nombre").toString();

        var provincia = new Provincia(nombre);

        provincias.add(provincia);
      }

      return provincias;

    } catch (LocalizacionApiException e) {
      throw new LocalizacionApiException("Error al intentar obtener las provincias");
    }
  }

  public List<Municipio> getMunicipios(List<Provincia> provincias) {
    try {
      var municipios = new ArrayList<Municipio>();

      var respuesta = consultarApi("https://apis.datos.gob.ar/georef/api/municipios.json");

      var array = (ArrayList) respuesta.get("municipios");

      for (var elemento : array) {
        var obj = (HashMap) elemento;
        var nombre = obj.get("nombre").toString();
        var nombreProvincia = ((HashMap) obj.get("provincia")).get("nombre").toString();

        var provincia = this.getProvincia(provincias, nombreProvincia);

        var municipio = new Municipio(nombre, provincia);

        municipios.add(municipio);
      }

      return municipios;

    } catch (LocalizacionApiException e) {
      throw new LocalizacionApiException("Error al intentar obtener los municipios");
    }
  }

  public List<Departamento> getDepartamentos(List<Provincia> provincias) {
    try {
      var departamentos = new ArrayList<Departamento>();

      var respuesta = consultarApi("https://apis.datos.gob.ar/georef/api/departamentos.json");

      var array = (ArrayList) respuesta.get("departamentos");

      for (var elemento : array) {
        var obj = (HashMap) elemento;
        var nombre = obj.get("nombre").toString();
        var nombreProvincia = ((HashMap) obj.get("provincia")).get("nombre").toString();

        var provincia = this.getProvincia(provincias, nombreProvincia);

        var departamento = new Departamento(nombre, provincia);

        departamentos.add(departamento);
      }

      return departamentos;

    } catch (LocalizacionApiException e) {
      throw new LocalizacionApiException("Error al intentar obtener los departamentos");
    }
  }

  public Map<String, Object> consultarApi(String url) {
    return this.client.target(url)
        .request(MediaType.APPLICATION_JSON)
        .get(Map.class);
  }

  private Provincia getProvincia(List<Provincia> provincias, String nombreProvincia) {
    return provincias.stream()
        .filter(prov -> nombreProvincia.equals(prov.getNombre()))
        .findAny()
        .orElse(null);
  }
}
