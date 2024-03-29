package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.excepciones.ArchivoCsvException;
import ar.edu.utn.frba.dds.importadores.ArchivoParseableCsv;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArchivoParseableCsvTest {
  @Test
  public void unPathInexistenteLanzaUnaExcepcion() {
    String mensajeEsperado = "El path proporcionado no es valido";
    newArchivoParseableCSVFalla("unPathInexistente.csv", mensajeEsperado);
  }

  @Test
  public void siNoEsCSVLanzaUnaExcepcion() {
    String mensajeEsperado = "El archivo no es un archivo CSV valido";
    newArchivoParseableCSVFalla("archivoEnOtroFormato.txt", mensajeEsperado);
  }

  public void newArchivoParseableCSVFalla(String fileName, String msg) {
    String filePath = obtenerPathAbsoluto(fileName);

    Exception exception = assertThrows(
        ArchivoCsvException.class,
        () -> new ArchivoParseableCsv(filePath)
    );

    assertEquals(msg, exception.getMessage());
  }

  public String obtenerPathAbsoluto(String nombreArchivo) {
    return Paths.get("src", "test", "resources", "csvDePrueba", nombreArchivo)
        .toAbsolutePath()
        .toString();
  }
}
