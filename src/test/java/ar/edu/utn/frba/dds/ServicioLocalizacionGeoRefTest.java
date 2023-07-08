package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ar.edu.utn.frba.dds.entidades.Localizacion;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeLocalizacion;
import ar.edu.utn.frba.dds.excepciones.LocalizacionNoExistenteException;
import ar.edu.utn.frba.dds.localizacion.implementaciones.ServicioLocalizacionGeoRef;
import ar.edu.utn.frba.dds.localizacion.apis.GeoRefApiCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

public class ServicioLocalizacionGeoRefTest {

  private GeoRefApiCliente apiCliente;
  private ServicioLocalizacionGeoRef servicio;

  @BeforeEach
  public void inicializarServicioGeoRef() {
    apiCliente = mock(GeoRefApiCliente.class);
    servicio = new ServicioLocalizacionGeoRef(apiCliente);
  }

  @Test
  public void testProvinciaExistente() {
    when(apiCliente.getProvinciaFromApi("Misiones"))
        .thenReturn(
            Map.ofEntries(
                Map.entry("nombre", "Misiones"),
                Map.entry("centroide_lat", (double) 0),
                Map.entry("centroide_lon", (double) 0)
            )
        );

    Localizacion provincia = servicio.getProvincia("Misiones");
    assertEquals("Misiones", provincia.getNombre());
    assertEquals(TipoDeLocalizacion.PROVINCIA, provincia.getTipo());
  }

  @Test
  public void testProvinciaInexistente() {
    when(apiCliente.getProvinciaFromApi(anyString()))
        .thenThrow(LocalizacionNoExistenteException.class);

    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getProvincia("Provincia inexistente"));
  }

  @Test
  public void testMunicipioExistente() {
    when(apiCliente.getMunicipioFromApi("Abra Pampa", "Jujuy"))
        .thenReturn(
            Map.ofEntries(
                Map.entry("nombre", "Abra Pampa"),
                Map.entry("centroide_lat", (double) 0),
                Map.entry("centroide_lon", (double) 0)
            )
        );

    Localizacion municipio = servicio.getMunicipio("Abra Pampa", "Jujuy");
    assertEquals("Abra Pampa", municipio.getNombre());
    assertEquals(TipoDeLocalizacion.MUNICIPIO, municipio.getTipo());
  }

  @Test
  public void testMunicipioInexistente() {
    when(apiCliente.getMunicipioFromApi(anyString(), anyString()))
        .thenThrow(LocalizacionNoExistenteException.class);

    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getMunicipio("Municipio inexistente", "Jujuy"));
  }

  @Test
  public void testDepartamentoExistente() {
    when(apiCliente.getDepartamentoFromApi("José C. Paz", "Buenos Aires"))
        .thenReturn(
            Map.ofEntries(
                Map.entry("nombre", "José C. Paz"),
                Map.entry("centroide_lat", (double) 0),
                Map.entry("centroide_lon", (double) 0)
            )
        );

    Localizacion departamento = servicio.getDepartamento("José C. Paz", "Buenos Aires");
    assertEquals("José C. Paz", departamento.getNombre());
    assertEquals(TipoDeLocalizacion.DEPARTAMENTO, departamento.getTipo());
  }

  @Test
  public void testDepartamentoInexistente() {
    when(apiCliente.getDepartamentoFromApi(anyString(), anyString()))
        .thenThrow(LocalizacionNoExistenteException.class);

    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getDepartamento("Departamento inexistente", "Buenos Aires"));
  }
}