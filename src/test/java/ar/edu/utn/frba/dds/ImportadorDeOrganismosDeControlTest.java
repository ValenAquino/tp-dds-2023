package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.excepciones.ArchivoCSVException;
import ar.edu.utn.frba.dds.importadores.ImportadorDeOrganismosDeControl;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ImportadorDeOrganismosDeControlTest {

  @Test
  public void puedoObtener20OrganismosDeControl() {
    assert20OrganismosLeidos("20organismos.csv");
  }

  @Test
  public void sePuedeLeerSiLasColumnasEstanEnOtroOrden() {
    assert20OrganismosLeidos("20organismosColumnasAlReves.csv");
  }

  @Test
  public void sePuedeLeerSiHayMasColumnas() {
    assert20OrganismosLeidos("20OrganismosMasColumnas.csv");
  }

  @Test
  public void unPathInexistenteLanzaUnaExcepcion() {
    String mensajeEsperado = "El path proporcionado no es valido";
    newImportadorDeOrganismosFalla("unPathInexistente.csv", mensajeEsperado);
  }

  @Test
  public void siNoEsCSVLanzaUnaExcepcion() {
    String mensajeEsperado = "El archivo no es un archivo CSV valido";
    newImportadorDeOrganismosFalla("archivoEnOtroFormato.txt", mensajeEsperado);
  }

  @Test
  public void siNotieneColumnaCorreoFalla() {
    String mensajeEsperado = "El archivo no contiene la columna correo";
    getOrganismosFalla("csvSinCampoCorreo.csv", mensajeEsperado);
  }

  @Test
  public void siNotieneColumnaNombreFalla() {
    String mensajeEsperado = "El archivo no contiene la columna nombre";
    getOrganismosFalla("csvSinCampoNombre.csv", mensajeEsperado);
  }

  @Test
  public void unArchivoVacioLanzaUnaExcepcion() {
    String mensajeEsperado = "El archivo esta vacio";
    getOrganismosFalla("archivoVacio.csv", mensajeEsperado);
  }

  @Test
  public void unArchivoSoloConHeadersLanzaUnaExcepcion() {
    String mensajeEsperado = "El archivo no contiene organismos de control";
    getOrganismosFalla("archivoSoloConHeaders.csv", mensajeEsperado);
  }

  @Test
  public void unCampoVacioFalla() {
    String mensajeEsperado = "El archivo contiene campos vacios";
    getOrganismosFalla("csvCampoCorreoVacio.csv", mensajeEsperado);
    getOrganismosFalla("csvCampoNombreVacio.csv", mensajeEsperado);
  }

  public void newImportadorDeOrganismosFalla(String fileName, String msg) {
    String filePath = obtenerPathAbsoluto(fileName);

    Exception exception = assertThrows(
        ArchivoCSVException.class,
        () -> new ImportadorDeOrganismosDeControl(filePath)
    );

    assertEquals(msg, exception.getMessage());
  }

  public void getOrganismosFalla(String fileName, String msg) {
    String filePath = obtenerPathAbsoluto(fileName);

    ImportadorDeOrganismosDeControl importador = new ImportadorDeOrganismosDeControl(filePath);

    Exception exception = assertThrows(
        ArchivoCSVException.class,
        importador::getOrganismosDeControl
    );

    assertEquals(msg, exception.getMessage());
  }

  public void assert20OrganismosLeidos(String fileName) {
    String filePath = obtenerPathAbsoluto(fileName);
    ImportadorDeOrganismosDeControl importador = new ImportadorDeOrganismosDeControl(filePath);

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
