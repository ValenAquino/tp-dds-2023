package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.excepciones.ArchivoCSVException;
import ar.edu.utn.frba.dds.importadores.ArchivoParseableCSV;
import ar.edu.utn.frba.dds.importadores.ImportadorDeOrganismosDeControl;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ImportadorDeOrganismosDeControlTest {

  @Test
  public void puedoObtener20OrganismosDeControl() {
    String path = obtenerPathAbsoluto("20organismos.csv");
    ArchivoParseableCSV archivo = new ArchivoParseableCSV(path);
    assert20OrganismosLeidos(archivo);
  }

  @Test
  public void sePuedeLeerSiLasColumnasEstanEnOtroOrden() {
    String path = obtenerPathAbsoluto("20organismosColumnasAlReves.csv");
    ArchivoParseableCSV archivo = new ArchivoParseableCSV(path);
    assert20OrganismosLeidos(archivo);
  }

  @Test
  public void sePuedeLeerSiHayMasColumnas() {
    String path = obtenerPathAbsoluto("20OrganismosMasColumnas.csv");
    ArchivoParseableCSV archivo = new ArchivoParseableCSV(path);
    assert20OrganismosLeidos(archivo);
  }

  @Test
  public void unCampoVacioFalla() {
    String mensajeEsperadoCorreo = "El correo no puede ser vacio";
    String mensajeEsperadoNombre = "El nombre no puede ser vacio";

    String path1 = obtenerPathAbsoluto("correoVacio.csv");
    String path2 = obtenerPathAbsoluto("nombreVacio.csv");

    ArchivoParseableCSV archivoSinCampoCorreo = new ArchivoParseableCSV(path1);
    ArchivoParseableCSV archivoSinCampoNombre = new ArchivoParseableCSV(path2);

    getOrganismosFalla(archivoSinCampoCorreo, mensajeEsperadoCorreo);
    getOrganismosFalla(archivoSinCampoNombre, mensajeEsperadoNombre);
  }

  @Test
  public void unCampoInvalidoFalla() {

  }

  @Test
  public void unCorreoInvalidoFalla() {
    String mensajeEsperado = "El archivo contiene un correo invalido";
    String path = obtenerPathAbsoluto("correoInvalido.csv");

    ArchivoParseableCSV archivo = new ArchivoParseableCSV(path);
    getOrganismosFalla(archivo, mensajeEsperado);
  }

  @Test
  public void unNombreInvalidoFalla() {
    String mensajeEsperado = "El archivo contiene un nombre invalido";
    String path = obtenerPathAbsoluto("nombreInvalido.csv");

    ArchivoParseableCSV archivo = new ArchivoParseableCSV(path);
    getOrganismosFalla(archivo, mensajeEsperado);
  }

  public void getOrganismosFalla(ArchivoParseableCSV archivo, String msg) {
    ImportadorDeOrganismosDeControl importador = new ImportadorDeOrganismosDeControl(archivo);

    Exception exception = assertThrows(
        ArchivoCSVException.class,
        importador::getOrganismosDeControl
    );

    assertEquals(msg, exception.getMessage());
  }

  public void assert20OrganismosLeidos(ArchivoParseableCSV archivo) {
    ImportadorDeOrganismosDeControl importador = new ImportadorDeOrganismosDeControl(archivo);

    List<OrganismoDeControl> organismosDeControl = importador.getOrganismosDeControl();

    assertNotNull(organismosDeControl);
    assertEquals(20, organismosDeControl.size());

    int i = 1;

    for (OrganismoDeControl organismo : organismosDeControl) {
      assertEquals("Organismo" + i, organismo.getNombre());
      assertEquals("organismo" + i + "@example.com", organismo.getCorreoElectronico());
      i++;
    }
  }

  public String obtenerPathAbsoluto(String nombreArchivo) {
    return Paths.get("src", "test", "resources", "csvDePrueba", nombreArchivo)
        .toAbsolutePath()
        .toString();
  }
}
