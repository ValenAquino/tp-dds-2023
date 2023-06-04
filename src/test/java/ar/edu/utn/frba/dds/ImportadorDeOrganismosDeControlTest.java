package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.excepciones.PathInvalidoException;
import ar.edu.utn.frba.dds.importadores.ImportadorDeOrganismosDeControl;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ImportadorDeOrganismosDeControlTest {

  private List<OrganismoDeControl> organismosDeControl;

  @Test
  public void unPathInexistenteLanzaUnaExcepcion() {
    String filePath = "unPathInexistente.csv";
    String mensajeEsperado = "El path proporcionado no es valido";

    Exception exception = assertThrows(PathInvalidoException.class, () -> {
      new ImportadorDeOrganismosDeControl(filePath);
    });

    assertEquals(mensajeEsperado, exception.getMessage());
  }

  @Test
  public void siNoEsCSVLanzaUnaExcepcion() {
    String filePath = obtenerPathAbsoluto("archivoEnOtroFormato.txt");
    String mensajeEsperado = "El archivo no es un archivo CSV valido";

    Exception exception = assertThrows(PathInvalidoException.class, () -> {
      new ImportadorDeOrganismosDeControl(filePath);
    });

    assertEquals(mensajeEsperado, exception.getMessage());
  }

//  @Test
//  public void puedoObtener20OrganismosDeControl() {
//    String filePath = obtenerPathAbsoluto("20organismos.csv");
//    ImportadorDeOrganismosDeControl importador = new ImportadorDeOrganismosDeControl(filePath);
//
//    organismosDeControl = importador.getOrganismosDeControl();
//
//    assertNotNull(organismosDeControl);
//    assertEquals(20, organismosDeControl.size());
//  }
//
//  @Test
//  public void seCarganBienLosNombres() {
//    String filePath = obtenerPathAbsoluto("20organismos.csv");
//    ImportadorDeOrganismosDeControl importador = new ImportadorDeOrganismosDeControl(filePath);
//
//    organismosDeControl = importador.getOrganismosDeControl();
//
//    assertEquals("Organismo12", organismosDeControl.get(11).getNombre());
//  }

  public String obtenerPathAbsoluto(String nombreArchivo) {
    return Paths.get("src", "test", "resources", nombreArchivo)
        .toAbsolutePath()
        .toString();
  }
}
