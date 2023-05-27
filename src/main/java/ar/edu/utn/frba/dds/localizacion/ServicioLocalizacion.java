package ar.edu.utn.frba.dds.localizacion;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ServicioLocalizacion {
  public List<Departamento> departamentos;
  public List<Municipio> municipios;
  public List<Provincia> provincias;

  public ServicioLocalizacion() {
    cargarProvincias();
    cargarMunicipios();
    cargarDepartamentos();
  }

  public Provincia getProvincia(String nombre) {
    var provincia = provincias.stream()
        .filter(prov -> nombre.equals(prov.getNombre()))
        .findAny()
        .orElse(null);

    if (provincia == null) {
      throw new LocalizacionNoExistenteException("Provincia no existente");
    }

    return provincia;
  }

  public Departamento getDepartamento(String nombre, String provinciaNombre) {
    var departamento = departamentos.stream()
        .filter(dep -> nombre.equals(dep.getNombre())
            && provinciaNombre.equals(dep.getProvinciaNombre()))
        .findAny()
        .orElse(null);

    if (departamento == null) {
      throw new LocalizacionNoExistenteException("Departamento no existente");
    }

    return departamento;
  }

  public Municipio getMunicipio(String nombre, String provinciaNombre) {
    var municipio = municipios.stream()
        .filter(mun -> nombre.equals(mun.getNombre())
            && provinciaNombre.equals(mun.getProvinciaNombre()))
        .findAny()
        .orElse(null);

    if (municipio == null) {
      throw new LocalizacionNoExistenteException("Municipio no existente");
    }

    return municipio;
  }

  private void cargarProvincias() {
    this.provincias = new ArrayList<>();

    try {
      var raiz = obtenerJsonRaiz("https://apis.datos.gob.ar/georef/api/provincias");

      var array = raiz.getAsJsonObject().get("provincias").getAsJsonArray();

      for (var elemento : array) {
        var json = elemento.getAsJsonObject();
        var nombre = json.get("nombre").getAsString();

        var provincia = new Provincia(nombre);

        provincias.add(provincia);
      }

    } catch (LocalizacionApiException e) {
      throw new LocalizacionApiException("Error al intentar obtener las provincias");
    }
  }

  private void cargarMunicipios() {
    try {
      this.municipios = new ArrayList<>();

      var raiz = obtenerJsonRaiz("https://apis.datos.gob.ar/georef/api/municipios.json");

      var array = raiz.getAsJsonObject().get("municipios").getAsJsonArray();

      for (var elemento : array) {
        var json = elemento.getAsJsonObject();
        var nombre = json.get("nombre").getAsString();
        var nombreProvincia = json.getAsJsonObject("provincia").get("nombre").getAsString();

        var provincia = this.getProvincia(nombreProvincia);

        var municipio = new Municipio(nombre, provincia);

        municipios.add(municipio);
      }

    } catch (LocalizacionApiException e) {
      throw new LocalizacionApiException("Error al intentar obtener los municipios");
    }
  }

  private void cargarDepartamentos() {
    try {
      this.departamentos = new ArrayList<>();

      var raiz = obtenerJsonRaiz("https://apis.datos.gob.ar/georef/api/departamentos.json");

      var array = raiz.getAsJsonObject().get("departamentos").getAsJsonArray();

      for (var elemento : array) {
        var json = elemento.getAsJsonObject();
        var nombre = json.get("nombre").getAsString();
        var nombreProvincia = json.getAsJsonObject("provincia").get("nombre").getAsString();

        var provincia = this.getProvincia(nombreProvincia);

        var departamento = new Departamento(nombre, provincia);

        departamentos.add(departamento);
      }

    } catch (LocalizacionApiException e) {
      throw new LocalizacionApiException("Error al intentar obtener los departamentos");
    }
  }

  private JsonElement obtenerJsonRaiz(String str) {
    try {
      var url = new URL(str);

      var response = url.openStream();

      var parser = new JsonParser();

      var obj = parser.parse(new InputStreamReader(response, Charset.defaultCharset()));

      return obj;
    } catch (IOException e) {
      throw new LocalizacionApiException("Error al llamar a GeoRef API");
    }
  }
}
