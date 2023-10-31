package ar.edu.utn.frba.dds.model.exportadores;

import ar.edu.utn.frba.dds.model.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.model.excepciones.GeneradorCsvException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class ExportadorRankingCsv {
  private final Ranking ranking;
  private final String[] header = {"Nro", "Entidad", "Valor"};

  public ExportadorRankingCsv(Ranking ranking) {
    this.ranking = ranking;
  }

  public String getNombreArchivo() {
    return String.format(
        "Ranking_%s_%s.csv",
        ranking.getDescripcionCriterio().replace(" ", "_"),
        ranking.getFecha().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"))
    );
  }

  public void exportar() {
    try (CSVPrinter csvPrinter = generarCsvPrinter()) {
      AtomicInteger puesto = new AtomicInteger(1);
      ranking.getEntidades().forEach((entidad, valor) ->
          {
            try {
              csvPrinter.printRecord(puesto.getAndIncrement(), entidad.getNombre(), valor);
            } catch (IOException e) {
              throw new GeneradorCsvException("Error al escribir en el archivo CSV: " + e.getMessage());
            }
          }
      );
      csvPrinter.flush();
    } catch (Exception e) {
      throw new GeneradorCsvException("Falló la generación del archivo CSV: " + e.getMessage());
    }
  }

  private CSVPrinter generarCsvPrinter() throws IOException {
    Path carpetaReportesPath = Paths.get("", "reportes");
    Files.createDirectories(carpetaReportesPath);

    return new CSVPrinter(
        Files.newBufferedWriter(
            Paths.get(carpetaReportesPath.resolve(getNombreArchivo()).toString())
        ),
        CSVFormat.Builder.create().setHeader(header).build()
    );
  }
}
