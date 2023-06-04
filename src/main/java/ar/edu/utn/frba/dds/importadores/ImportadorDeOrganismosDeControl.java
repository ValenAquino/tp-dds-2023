package ar.edu.utn.frba.dds.importadores;

import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.excepciones.ArchivoCSVException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ImportadorDeOrganismosDeControl {
  private final String pathCsv;

  public ImportadorDeOrganismosDeControl(String path) {
    validarPath(path);
    this.pathCsv = path;
  }

  private void validarPath(String path) {
    Path ruta = Paths.get(path);

    // Checkea que el path sea un archivo existente
    // que sea accesible, no sea un directorio, ni un enlace simbólico
    if (!Files.isRegularFile(ruta)) {
      throw new ArchivoCSVException("El path proporcionado no es valido");
    }

    if (!path.toLowerCase().endsWith(".csv")) {
      throw new ArchivoCSVException("El archivo no es un archivo CSV valido");
    }
  }

  public List<OrganismoDeControl> getOrganismosDeControl() {
    List<OrganismoDeControl> organismos = new ArrayList<>();

    try {
      CSVParser csvParser = getCsvParser();
      List<CSVRecord> csvRecords = getCSVRecordsValidadas(csvParser);

      // Buscar los índices de las columnas "nombre" y "correo"
      CSVRecord headerRecord = csvRecords.remove(0);
      int nombreIndex = headerRecord.toList().indexOf("nombre");
      int correoIndex = headerRecord.toList().indexOf("correo");

      for (CSVRecord csvRecord : csvRecords) {
        String nombre = csvRecord.get(nombreIndex);
        String correo = csvRecord.get(correoIndex);

        organismos.add(organismoDeControl(nombre, correo));
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return organismos;
  }

  public OrganismoDeControl organismoDeControl(String nombre, String correo) {
    if (nombre.isEmpty() || correo.isEmpty()) {
      throw new ArchivoCSVException("El archivo contiene campos vacios");
    }

    return new OrganismoDeControl(nombre, correo);
  }

  public List<CSVRecord> getCSVRecordsValidadas(CSVParser csvParser) {
    List<CSVRecord> csvRecords = csvParser.getRecords();

    if (csvRecords.size() == 0) {
      throw new ArchivoCSVException("El archivo esta vacio");
    }

    String header = Arrays.toString(csvRecords.get(0).values());

    if (csvRecords.size() == 1) {
      throw new ArchivoCSVException("El archivo no contiene organismos de control");
    }

    if (!header.contains("nombre")) {
      throw new ArchivoCSVException("El archivo no contiene la columna nombre");
    }

    if (!header.contains("correo")) {
      throw new ArchivoCSVException("El archivo no contiene la columna correo");
    }

    return csvRecords;
  }

  public CSVParser getCsvParser() throws IOException {
    Reader reader = new FileReader(pathCsv);
    CSVFormat csvFormat = CSVFormat.Builder.create()
        .setHeader("nombre", "correo")
        .build();

    return new CSVParser(reader, csvFormat);
  }

}
