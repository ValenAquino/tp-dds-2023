package ar.edu.utn.frba.dds.exportadores;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.excepciones.GeneradorCsvException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

public class ExportadorRankingCsv {
  private final Ranking ranking;
  private final String[] header = {"Nro", "Entidad", "Valor"};

  public ExportadorRankingCsv(Ranking ranking) {
    this.ranking = ranking;
  }

  public String getNombreArchivo() {
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    String fechaFormateada = ranking.getFecha().format(formato);
    String criterio = ranking.getDescripcionCriterio().replace(" ", "_");

    return String.format(
        "Ranking_%s_%s.csv", criterio, fechaFormateada
    );
  }

  public void exportar() {
    try {
      CSVPrinter csvPrinter = generarCsvPrinter();

      int puesto = 1;
      for (Map.Entry<Entidad, Double> entry : ranking.getEntidades().entrySet()) {
        Entidad entidad = entry.getKey();
        Double valor = entry.getValue();
        csvPrinter.printRecord(Arrays.asList(puesto, entidad.getNombre(), valor));
        puesto++;
      }

      csvPrinter.flush();
    } catch (Exception e) {
      throw new GeneradorCsvException("Fallo la generacion del csv: " + e.getMessage());
    }
  }

  private CSVPrinter generarCsvPrinter() throws IOException {
    String rutaCompleta = this.construirRutaCompleta();
    BufferedWriter writer = Files.newBufferedWriter(Paths.get(rutaCompleta));
    CSVFormat csvFormat = CSVFormat.Builder.create().setHeader(header).build();
    return new CSVPrinter(writer, csvFormat);
  }

  private String construirRutaCompleta() throws IOException {
    String pathDirectorio = this.crearDirectorioReportesSiNoExiste();
    String nombreArchivo = this.getNombreArchivo();

    System.out.println(nombreArchivo);

    return pathDirectorio + nombreArchivo;
  }

  private String crearDirectorioReportesSiNoExiste() throws IOException {
    String subDirectorio = "/reportes/";
    String carpetaReportes = Paths.get("").toAbsolutePath() + subDirectorio;
    Path carpetaReportesPath = Paths.get(carpetaReportes);

    if (!Files.exists(carpetaReportesPath)) {
      Files.createDirectories(carpetaReportesPath);
    }

    return carpetaReportes;
  }
}
