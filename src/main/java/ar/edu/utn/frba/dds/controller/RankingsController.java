package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Entidad;
import ar.edu.utn.frba.dds.model.entidades.rankings.CriterioDeOrdenamiento;
import ar.edu.utn.frba.dds.model.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.model.entidades.rankings.criterios.CantidadIncidentes;
import ar.edu.utn.frba.dds.model.entidades.rankings.criterios.MayorPromedioCierre;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingsController implements WithSimplePersistenceUnit {
  public ModelAndView renderRanking(CriterioDeOrdenamiento criterio) {
    Ranking ranking = new Ranking(RepositorioIncidentes.getInstance(), criterio);
    ranking.generarRanking();

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

}
