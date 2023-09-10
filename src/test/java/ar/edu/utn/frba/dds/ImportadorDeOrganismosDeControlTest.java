package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.excepciones.ArchivoCsvException;
import ar.edu.utn.frba.dds.importadores.ArchivoParseableCsv;
import ar.edu.utn.frba.dds.importadores.ImportadorDeOrganismosDeControl;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImportadorDeOrganismosDeControlTest {

  @Test
  public void puedoObtener20OrganismosDeControl() {
    String path = obtenerPathAbsoluto("20organismos.csv");
    ArchivoParseableCsv archivo = new ArchivoParseableCsv(path);
    assert20OrganismosLeidos(archivo);
  }

  @Test
  public void sePuedeLeerSiLasColumnasEstanEnOtroOrden() {
    String path = obtenerPathAbsoluto("20organismosColumnasAlReves.csv");
    ArchivoParseableCsv archivo = new ArchivoParseableCsv(path);
    assert20OrganismosLeidos(archivo);
  }

  @Test
  public void sePuedeLeerSiHayMasColumnas() {
    String path = obtenerPathAbsoluto("20OrganismosMasColumnas.csv");
    ArchivoParseableCsv archivo = new ArchivoParseableCsv(path);
    assert20OrganismosLeidos(archivo);
  }

  @Test
  public void unCampoVacioFalla() {
    String mensajeEsperadoCorreo = "El correo no puede ser vacio";
    String mensajeEsperadoNombre = "El nombre no puede ser vacio";

    String path1 = obtenerPathAbsoluto("correoVacio.csv");
    String path2 = obtenerPathAbsoluto("nombreVacio.csv");

    ArchivoParseableCsv archivoSinCampoCorreo = new ArchivoParseableCsv(path1);
    ArchivoParseableCsv archivoSinCampoNombre = new ArchivoParseableCsv(path2);

    getOrganismosFalla(archivoSinCampoCorreo, mensajeEsperadoCorreo);
    getOrganismosFalla(archivoSinCampoNombre, mensajeEsperadoNombre);
  }

  @Test
  public void seParseanCaracteresDeUTF8() {
    String path = obtenerPathAbsoluto("utf8.csv");
    ArchivoParseableCsv archivo = new ArchivoParseableCsv(path);

    ImportadorDeOrganismosDeControl importador = new ImportadorDeOrganismosDeControl(archivo);
    List<OrganismoDeControl> organismos = importador.getOrganismosDeControl();

    assertEquals("ñandu", organismos.get(0).getNombre());
    assertEquals("emáil@dominó.com", organismos.get(0).getCorreoElectronico());
  }

  @Test
  public void unCorreoInvalidoFalla() {
    String mensajeEsperado = "El archivo contiene un correo invalido";
    String path = obtenerPathAbsoluto("correoInvalido.csv");

    ArchivoParseableCsv archivo = new ArchivoParseableCsv(path);
    getOrganismosFalla(archivo, mensajeEsperado);
  }

  @Test
  public void unNombreInvalidoFalla() {
    String mensajeEsperado = "El archivo contiene un nombre invalido";
    String path = obtenerPathAbsoluto("nombreInvalido.csv");

    ArchivoParseableCsv archivo = new ArchivoParseableCsv(path);
    getOrganismosFalla(archivo, mensajeEsperado);
  }

  public void getOrganismosFalla(ArchivoParseableCsv archivo, String msg) {
    ImportadorDeOrganismosDeControl importador = new ImportadorDeOrganismosDeControl(archivo);

    Exception exception = assertThrows(
        ArchivoCsvException.class,
        importador::getOrganismosDeControl
    );

    assertEquals(msg, exception.getMessage());
  }

  public void assert20OrganismosLeidos(ArchivoParseableCsv archivo) {
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
