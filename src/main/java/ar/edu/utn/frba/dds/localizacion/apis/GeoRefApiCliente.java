package ar.edu.utn.frba.dds.localizacion.apis;

import ar.edu.utn.frba.dds.excepciones.LocalizacionApiException;
import ar.edu.utn.frba.dds.excepciones.LocalizacionNoExistenteException;
import java.util.ArrayList;
import java.util.Map;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class GeoRefApiCliente {

  public Map<String, Object> getProvinciaFromApi(String nombre) {
    return consultarApi("provincias", nombre, null);
  }

  public Map<String, Object> getMunicipioFromApi(String nombre, String provinciaNombre) {
    return consultarApi("municipios", nombre, provinciaNombre);
  }

  public Map<String, Object> getDepartamentoFromApi(String nombre, String provinciaNombre) {
    return consultarApi("departamentos", nombre, provinciaNombre);
  }

  private static Map<String, Object> consultarApi(String path, String nombre, String provincia) {
    ArrayList responseBody;

    try {
      var requestBody = ClientBuilder.newClient().target("https://apis.datos.gob.ar/georef/api")
          .path(path)
          .queryParam("nombre", nombre)
          .queryParam("aplanar", "");

      if (provincia != null) {
        requestBody = requestBody.queryParam("provincia", provincia);
      }

      responseBody = (ArrayList) requestBody
          .request(MediaType.APPLICATION_JSON)
          .get(Map.class)
          .get(path);

    } catch (Exception e) {
      throw new LocalizacionApiException(e.getMessage());
    }

    if (responseBody.isEmpty()) {
      throw new LocalizacionNoExistenteException(String.format("No se encontraron %s", path));
    }

    return (Map<String, Object>) responseBody.get(0);
  }
}