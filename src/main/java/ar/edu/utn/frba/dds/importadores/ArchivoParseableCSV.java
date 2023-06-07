package ar.edu.utn.frba.dds.importadores;

import ar.edu.utn.frba.dds.excepciones.ArchivoCSVException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ArchivoParseableCSV {
  private final String path;
  private final String[] header = {"nombre", "correo"};

  public ArchivoParseableCSV(String path) {
    validarPath(path);
    this.path = path;
  }

  public void validarPath(String path) {
    vaildarArchivoRegular(path);
    validarExtensionCSV(path);
  }

  public void vaildarArchivoRegular(String path) {
    Path ruta = Paths.get(path);
    if (!Files.isRegularFile(ruta)) {
      throw new ArchivoCSVException("El path proporcionado no es valido");
    }
  }

  public void validarExtensionCSV(String path) {
    if (!path.toLowerCase().endsWith(".csv")) {
      throw new ArchivoCSVException("El archivo no es un archivo CSV valido");
    }
  }

  public CSVParser csvParser() throws IOException {
    Reader reader = new FileReader(path);
    CSVFormat csvFormat = CSVFormat.Builder.create().setHeader(header).build();
    return new CSVParser(reader, csvFormat);
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
      throw new ArchivoCSVException("El archivo no contiene la columna " + campo);
    }
  }

  public void archivoNoVacio(List<CSVRecord> csvRecords) {
    if (csvRecords.size() <= 1) {
      throw new ArchivoCSVException("El archivo esta vacio");
    }
  }

}
