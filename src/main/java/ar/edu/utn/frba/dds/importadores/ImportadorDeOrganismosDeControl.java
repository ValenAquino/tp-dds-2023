package ar.edu.utn.frba.dds.importadores;

import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.excepciones.PathInvalidoException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class ImportadorDeOrganismosDeControl {

  private final String pathCsv;

  public ImportadorDeOrganismosDeControl(String path) {
    validarPath(path);
    this.pathCsv = path;
  }

  private void validarPath(String path) {
    Path ruta = Paths.get(path);

    if (!Files.isRegularFile(ruta)) {
      // Checkea que el path sea un archivo existente
      // que sea accesible, no sea un directorio, ni un enlace simbólico
      throw new PathInvalidoException("El path proporcionado no es valido");
    }

    if (!path.toLowerCase().endsWith(".csv")) {
      throw new PathInvalidoException("El archivo no es un archivo CSV valido");
    }
  }

  public List<OrganismoDeControl> getOrganismosDeControl() {
    List<OrganismoDeControl> organismos = new ArrayList<>();

    try (Reader reader = new FileReader(pathCsv);
         CSVParser csvParser = CSVParser.parse(
             reader,
             CSVFormat.DEFAULT.withHeader("nombre", "correo"))
    ) {
      csvParser.forEach(csvRecord -> {
        String nombre = csvRecord.get("nombre");
        String correo = csvRecord.get("correo");
        organismos.add(new OrganismoDeControl(nombre, correo));
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return organismos;
  }
}
