package ar.edu.utn.frba.dds.localizacion.apis;

import ar.edu.utn.frba.dds.excepciones.LocalizacionApiException;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class GeoRefApiCliente {
  private final Client client = ClientBuilder.newClient();

  public Map<String, Object> getProvinciaFromApi(String nombre) {
    return consultarApi("provincias", nombre, null, "Error al intentar obtener las provincias");
  }

  public Map<String, Object> getMunicipioFromApi(String nombre, String provinciaNombre) {
    return consultarApi("municipios", nombre, provinciaNombre, "Error al intentar obtener los municipios");
  }

  public Map<String, Object> getDepartamentoFromApi(String nombre, String provinciaNombre) {
    return consultarApi("departamentos", nombre, provinciaNombre, "Error al intentar obtener los departamentos");
  }

  public Map<String, Object> consultarApi(String path, String nombre, String provinciaNombre, String errorMessage) {
    try {
      var requestBody = this.client.target("https://apis.datos.gob.ar/georef/api")
          .path(path)
          .queryParam("nombre", nombre)
          .queryParam("aplanar", "");

      if (provinciaNombre != null) {
        requestBody = requestBody.queryParam("provincia", provinciaNombre);
      }

      return requestBody
          .request(MediaType.APPLICATION_JSON)
          .get(Map.class);

    } catch (LocalizacionApiException e) {
      throw new LocalizacionApiException(errorMessage);
    }
  }
}
