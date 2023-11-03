package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Entidad;
import ar.edu.utn.frba.dds.model.entidades.rankings.CriterioDeOrdenamiento;
import ar.edu.utn.frba.dds.model.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.model.entidades.rankings.criterios.CantidadIncidentes;
import ar.edu.utn.frba.dds.model.entidades.rankings.criterios.MayorPromedioCierre;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.model.excepciones.GeneradorCsvException;
import ar.edu.utn.frba.dds.model.exportadores.ExportadorRankingCsv;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingsController implements WithSimplePersistenceUnit {
  public ModelAndView renderRanking(CriterioDeOrdenamiento criterio) {
    Ranking ranking = new Ranking(RepositorioIncidentes.getInstance(), criterio);
    ranking.generarRanking();

    ranking.getEntidades().forEach((entidad, valor) ->
        System.out.println(entidad.getNombre() + " - " + valor)
    );

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("criterio", criterio.getDescripcion());
    modelo.put("entidades", formatearRanking(ranking.getEntidades(), criterio));

    return new ModelAndView(modelo, "pages/incidentes/ranking.html.hbs");
  }

  public ModelAndView renderCantidadIncidentes(Request request, Response response) {
    return renderRanking(new CantidadIncidentes());
  }

  public ModelAndView renderMayorPromedioCierre(Request request, Response response) {
    return renderRanking(new MayorPromedioCierre());
  }

  private List<Map<String, Object>> formatearRanking(Map<Entidad, Double> entidades, CriterioDeOrdenamiento criterio) {
    List<Map<String, Object>> results = new ArrayList<>();

    entidades.forEach((entidad, valor) -> {
      Map<String, Object> result = new HashMap<>();
      result.put("entidad", entidad.getNombre());
      result.put(
          "valor",
          criterio instanceof MayorPromedioCierre
              ? valor / 60000.0
              : valor.intValue() // TODO: Enviar un entero para que no aparezca la parte decimal
      );
      results.add(result);
    });
    return results;
  }

  private String exportarRanking(CriterioDeOrdenamiento criterio) {
    Ranking ranking = new Ranking(RepositorioIncidentes.getInstance(), criterio);
    ranking.generarRanking();

    ExportadorRankingCsv exportador = new ExportadorRankingCsv(ranking);
    exportador.exportar();

    return leerContenidoArchivo(exportador.getNombreArchivo());
  }

  public String exportarCantidadIncidentes(Request request, Response response) {
    String contenido = exportarRanking(new CantidadIncidentes());
    configurarRespuesta(response, "Cantidad_Incidentes");
    return contenido;
  }

  public String exportarMayorPromedioCierre(Request request, Response response) {
    String contenido = exportarRanking(new MayorPromedioCierre());
    configurarRespuesta(response, "Mayor_Promedio_Cierre");
    return contenido;
  }

  private void configurarRespuesta(Response response, String criterio) {
    response.header("Content-Disposition", "attachment; filename=Ranking_" + criterio + ".csv");
    response.type("text/csv");
  }

  private String leerContenidoArchivo(String nombreArchivo) {
    try {
      Path filePath = Paths.get("reportes", nombreArchivo);
      return new String(Files.readAllBytes(filePath));
    } catch (IOException e) {
      throw new GeneradorCsvException("Error al leer el archivo CSV: " + e.getMessage());
    }
  }


}
