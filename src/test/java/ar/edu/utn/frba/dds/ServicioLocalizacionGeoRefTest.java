package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.excepciones.LocalizacionNoExistenteException;
import ar.edu.utn.frba.dds.localizacion.implementaciones.ServicioLocalizacionGeoRef;
import ar.edu.utn.frba.dds.localizacion.apis.GeoRefApiCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServicioLocalizacionGeoRefTest {

  private ServicioLocalizacionGeoRef servicio;

  @BeforeEach
  public void inicializarServicioGeoRef() {
    servicio = new ServicioLocalizacionGeoRef(new GeoRefApiCliente());
  }

  @Test
  public void testProvinciaExistente() {
    assertDoesNotThrow(() -> servicio.getProvincia("Misiones"));
  }

  @Test
  public void testProvinciaInexistente() {
    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getProvincia("Provincia inexistente"));
  }

  @Test
  public void testMunicipioExistente() {
    assertDoesNotThrow(() -> servicio.getMunicipio("Abra Pampa", "Jujuy"));
  }

  @Test
  public void testMunicipioInexistente() {
    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getMunicipio("Municipio inexistente", "Jujuy"));
  }

  @Test
  public void testDepartamentoExistente() {
    assertDoesNotThrow(() -> servicio.getDepartamento("José C. Paz", "Buenos Aires"));
  }

  @Test
  public void testDepartamentoInexistente() {
    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getDepartamento("Departamento inexistente", "Buenos Aires"));
  }
}