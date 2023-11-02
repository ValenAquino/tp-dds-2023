package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class IncidentesController implements WithSimplePersistenceUnit  {
  public ModelAndView render(Request request, Response response) {
    Map<String, Object> modelo = getIncidentes();
    return new ModelAndView(modelo, "incidentes/incidentes.html.hbs");
  }

  private Map<String, Object> getIncidentes() {
    var incidentes = RepositorioIncidentes.getInstance().todos();

    Map<String, Object> modelo = new HashMap<>();

    List<Map<String, Object>> results = new ArrayList<>();

    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    for (Incidente incidente : incidentes) {
     Map<String, Object> result = new HashMap<>();

     result.put("servicio", incidente.getServicio());
     result.put("establecimiento", incidente.getEstablecimiento());

      result.put("fecha", incidente.getFecha().format(format) + " hs");
      result.put("reportante", incidente.getReportante().getUsuario());
      result.put("abierto", !incidente.estaResuelto());
      result.put("observaciones", incidente.getObservaciones());

     results.add(result);
    }

    modelo.put("incidentes", results);

    return modelo;
  }
}
