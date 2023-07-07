package ar.edu.utn.frba.dds.exportadores;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.excepciones.GeneradorCsvException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Map;

public class GeneradorReportesCsv {
  private final String path = "./reportes/";
  private Ranking ranking;

  public GeneradorReportesCsv(Ranking ranking) {
    this.ranking = ranking;
  }

  public void exportar() {
    var nombre = "Ranking_" + this.ranking.getFecha().toString() + ".csv";
    var entidades = this.ranking.getEntidades();
    crearCsv(entidades, nombre);
  }

  private void crearCsv(Map<Entidad, Double> entidades, String nombre) {
    try {
      FileWriter fileWriter = new FileWriter(this.path + nombre);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

      String header = "Posicion,Nombre,Valor";
      bufferedWriter.write(header);
      bufferedWriter.newLine();

      Integer posicion = 1;

      for (var entry : entidades.entrySet()) {
        var entidad = entry.getKey();
        var valor = entry.getValue();

        var data = new String[]
            { posicion.toString(), entidad.getNombre(), valor.toString() };

        var dataLine =
            String.join(",", data);

        bufferedWriter.write(dataLine);
        bufferedWriter.newLine();

        posicion++;
      }

      bufferedWriter.close();
    }
    catch(Exception e) {
      throw new GeneradorCsvException("Ocurrió un error al generar el archivo csv");
    }
  }
}
