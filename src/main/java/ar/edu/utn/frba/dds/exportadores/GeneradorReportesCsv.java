package ar.edu.utn.frba.dds.exportadores;

import ar.edu.utn.frba.dds.entidades.rankings.Ranking;

public class GeneradorReportesCsv {
  private final String path = "exports/";
  private Ranking ranking;

  public GeneradorReportesCsv(Ranking ranking) {
    this.ranking = ranking;
  }

  public void exportar() {
  }
}
