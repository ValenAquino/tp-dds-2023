package ar.edu.utn.frba.dds.exportadores;

import ar.edu.utn.frba.dds.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.excepciones.GeneradorCsvException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class GeneradorReportesCsv {
  private String path = "./reportes/";
  private final Ranking ranking;

  public GeneradorReportesCsv(Ranking ranking) {
    this.ranking = ranking;
  }

  public void setPath(String path) {
    this.path = path;
  }

  private String getNombreArchivo() {
    return String.format(
        "Ranking_%s_%s.csv",
        ranking.getDescripcionCriterio(),
        ranking.getFecha());
  }

  private void exportar() {
    try {
      FileWriter fileWriter = new FileWriter(path + getNombreArchivo());
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

      String header = "Posicion,Nombre,Valor";
      bufferedWriter.write(header);
      bufferedWriter.newLine();

      int posicion = 1;

      for (var entry : ranking.getEntidades().entrySet()) {
        var entidad = entry.getKey();
        var valor = entry.getValue();

        var data = new String[]
            {Integer.toString(posicion), entidad.getNombre(), valor.toString()};

        var dataLine = String.join(",", data);

        bufferedWriter.write(dataLine);
        bufferedWriter.newLine();

        posicion++;
      }

      bufferedWriter.close();
    } catch (Exception e) {
      throw new GeneradorCsvException("Ocurrió un error al generar el archivo csv");
    }
  }
}
