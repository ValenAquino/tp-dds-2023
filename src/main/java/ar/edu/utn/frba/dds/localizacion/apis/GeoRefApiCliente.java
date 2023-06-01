package ar.edu.utn.frba.dds.localizacion.apis;

import ar.edu.utn.frba.dds.excepciones.LocalizacionApiException;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class GeoRefApiCliente {
  private String baseUrl = "https://apis.datos.gob.ar/georef/api";
  private Client client = ClientBuilder.newClient();

  public Map<String, Object> getProvinciaFromApi(String nombre) {
    try {
      return consultarApi("provincias", nombre, null);

    } catch (LocalizacionApiException e) {
      throw new LocalizacionApiException("Error al intentar obtener las provincias");
    }
  }

  public Map<String, Object> getMunicipioFromApi(String nombre, String provinciaNombre) {
    try {
      return consultarApi("municipios", nombre, provinciaNombre);

    } catch (LocalizacionApiException e) {
      throw new LocalizacionApiException("Error al intentar obtener los municipios");
    }
  }

  public Map<String, Object> getDepartamentoFromApi(String nombre, String provinciaNombre) {
    try {
      return consultarApi("departamentos", nombre, provinciaNombre);

    } catch (LocalizacionApiException e) {
      throw new LocalizacionApiException("Error al intentar obtener los departamentos");
    }
  }

  public Map<String, Object> consultarApi(String path, String nombre, String provinciaNombre) {
    var requestBody = this.client.target(baseUrl)
            .path(path)
            .queryParam("nombre", nombre)
            .queryParam("aplanar", "");

    if (provinciaNombre != null) {
      requestBody = requestBody.queryParam("provincia", provinciaNombre);
    }

    return requestBody
            .request(MediaType.APPLICATION_JSON)
            .get(Map.class);
  }
}
