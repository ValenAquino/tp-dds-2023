package ar.edu.utn.frba.dds.model.importadores;

import ar.edu.utn.frba.dds.model.excepciones.ArchivoCsvException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ArchivoParseableCsv {
  private final String path;
  private final String[] header = {"nombre", "correo"};

  public ArchivoParseableCsv(String path) {
    validarPath(path);
    this.path = path;
  }

  public void validarPath(String path) {
    vaildarArchivoRegular(path);
    validarExtensionCsv(path);
  }

  public void vaildarArchivoRegular(String path) {
    Path ruta = Paths.get(path);
    if (!Files.isRegularFile(ruta)) {
      throw new ArchivoCsvException("El path proporcionado no es valido");
    }
  }

  public void validarExtensionCsv(String path) {
    if (!path.toLowerCase().endsWith(".csv")) {
      throw new ArchivoCsvException("El archivo no es un archivo CSV valido");
    }
  }

  public CSVParser csvParser() throws IOException {
    InputStream inputStream = new FileInputStream(path);
    CSVFormat csvFormat = CSVFormat.Builder.create().setHeader(header).build();
    return CSVParser.parse(inputStream, StandardCharsets.UTF_8, csvFormat);
  }

  public List<CSVRecord> getRecordsValidas() throws IOException {
    List<CSVRecord> csvRecords = csvParser().getRecords();
    archivoNoVacio(csvRecords);
    headerCompleto(Arrays.toString(csvRecords.get(0).values()));
    return csvRecords;
  }

  public void headerCompleto(String header) {
    for (String campo : this.header) {
      headerConCampo(campo, header);
    }
  }

  public void headerConCampo(String campo, String header) {
    if (!header.contains(campo)) {
      throw new ArchivoCsvException("El archivo no contiene la columna " + campo);
    }
  }

  public void archivoNoVacio(List<CSVRecord> csvRecords) {
    if (csvRecords.size() <= 1) {
      throw new ArchivoCsvException("El archivo esta vacio");
    }
  }

}
