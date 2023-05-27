package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.localizacion.LocalizacionNoExistenteException;
import ar.edu.utn.frba.dds.localizacion.ServicioLocalizacion;
import ar.edu.utn.frba.dds.politicas.PoliticaContrasenasExcluidas;
import ar.edu.utn.frba.dds.politicas.PoliticaLongitud;
import ar.edu.utn.frba.dds.politicas.PoliticaRegex;
import ar.edu.utn.frba.dds.validacion.PoliticaContrasena;
import ar.edu.utn.frba.dds.validacion.ValidacionContrasenaException;
import ar.edu.utn.frba.dds.validacion.ValidadorContrasena;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ServicioLocalizacionTest {

  @Test
  public void testProvinciaExistente() {
    var servicio = new ServicioLocalizacion();

    assertDoesNotThrow(() -> servicio.getProvincia("Misiones"));
  }

  @Test
  public void testProvinciaInexistente() {
    var servicio = new ServicioLocalizacion();

    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getProvincia("Provincia inexistente"));
  }

  @Test
  public void testMunicipioExistente() {
    var servicio = new ServicioLocalizacion();

    assertDoesNotThrow(() -> servicio.getMunicipio("Abra Pampa", "Jujuy"));
  }

  @Test
  public void testMunicipioInexistente() {
    var servicio = new ServicioLocalizacion();

    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getMunicipio("Municipio inexistente", "Jujuy"));
  }

  @Test
  public void testDepartamentoExistente() {
    var servicio = new ServicioLocalizacion();

    assertDoesNotThrow(() -> servicio.getDepartamento("José C. Paz", "Buenos Aires"));
  }

  @Test
  public void testDepartamentoInexistente() {
    var servicio = new ServicioLocalizacion();

    assertThrows(LocalizacionNoExistenteException.class,
        () -> servicio.getDepartamento("Departamento inexistente", "Buenos Aires"));
  }
}