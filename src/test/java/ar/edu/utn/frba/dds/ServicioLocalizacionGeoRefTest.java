package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.excepciones.LocalizacionNoExistenteException;
import ar.edu.utn.frba.dds.localizacion.implementaciones.ServicioLocalizacionGeoRef;
import ar.edu.utn.frba.dds.localizacion.apis.GeoRefApiCliente;
import org.junit.jupiter.api.Test;

public class ServicioLocalizacionGeoRefTest {

  @Test
  public void testProvinciaExistente() {

    var cliente = new GeoRefApiCliente();
    var servicio = new ServicioLocalizacionGeoRef(cliente);

    assertDoesNotThrow(() -> servicio.getProvincia("Misiones"));
  }

  @Test
  public void testProvinciaInexistente() {
    var cliente = new GeoRefApiCliente();
    var servicio = new ServicioLocalizacionGeoRef(cliente);

    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getProvincia("Provincia inexistente"));
  }

  @Test
  public void testMunicipioExistente() {
    var cliente = new GeoRefApiCliente();
    var servicio = new ServicioLocalizacionGeoRef(cliente);

    assertDoesNotThrow(() -> servicio.getMunicipio("Abra Pampa", "Jujuy"));
  }

  @Test
  public void testMunicipioInexistente() {
    var cliente = new GeoRefApiCliente();
    var servicio = new ServicioLocalizacionGeoRef(cliente);

    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getMunicipio("Municipio inexistente", "Jujuy"));
  }

  @Test
  public void testDepartamentoExistente() {
    var cliente = new GeoRefApiCliente();
    var servicio = new ServicioLocalizacionGeoRef(cliente);

    assertDoesNotThrow(() -> servicio.getDepartamento("José C. Paz", "Buenos Aires"));
  }

  @Test
  public void testDepartamentoInexistente() {
    var cliente = new GeoRefApiCliente();
    var servicio = new ServicioLocalizacionGeoRef(cliente);

    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getDepartamento("Departamento inexistente", "Buenos Aires"));
  }
}