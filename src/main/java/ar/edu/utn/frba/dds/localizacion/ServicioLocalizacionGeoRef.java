package ar.edu.utn.frba.dds.localizacion;

import ar.edu.utn.frba.dds.localizacion.apis.GeoRefApiCliente;
import java.util.List;

public class ServicioLocalizacionGeoRef implements ServicioLocalizacion {
  private GeoRefApiCliente apiCliente;
  private List<Departamento> departamentos;
  private List<Municipio> municipios;
  private List<Provincia> provincias;

  public ServicioLocalizacionGeoRef(GeoRefApiCliente apiCliente) {
    this.apiCliente = apiCliente;
    this.provincias = this.apiCliente.getProvincias();
    this.municipios = this.apiCliente.getMunicipios(this.provincias);
    this.departamentos = this.apiCliente.getDepartamentos(this.provincias);
  }

  public Provincia getProvincia(String nombre) {
    var provincia = provincias.stream()
        .filter(prov -> nombre.equals(prov.getNombre()))
        .findAny()
        .orElse(null);

    if (provincia == null) {
      throw new LocalizacionNoExistenteException("Provincia inexistente");
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
      throw new LocalizacionNoExistenteException("Departamento inexistente");
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
      throw new LocalizacionNoExistenteException("Municipio inexistente");
    }

    return municipio;
  }
}
