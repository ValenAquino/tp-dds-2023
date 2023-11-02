package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncidentesController implements WithSimplePersistenceUnit {
  public ModelAndView listarPorComunidad(Request request, Response response) {
    var comunidad = RepositorioComunidades.getInstance()
        .porId(Integer.valueOf(request.params("id")));

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("incidentes", formatearIncidentes(comunidad.getIncidentes()));
    return new ModelAndView(modelo, "incidentes/incidentes.html.hbs");
  }

  private List<Map<String, Object>> formatearIncidentes(List<Incidente> incidentes) {
    List<Map<String, Object>> results = new ArrayList<>();

    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    incidentes.forEach(incidente ->
        results.add(new HashMap<>() {{
          put("servicio", incidente.getServicio());
          put("establecimiento", incidente.getEstablecimiento());
          put("fecha", incidente.getFecha().format(format) + " hs");
          put("reportante", incidente.getReportante().getUsuario());
          put("abierto", !incidente.estaResuelto());
          put("observaciones", incidente.getObservaciones());
        }})
    );

    return results;
  }
}
