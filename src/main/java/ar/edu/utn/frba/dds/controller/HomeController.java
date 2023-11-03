package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Comunidad;
import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class HomeController implements WithSimplePersistenceUnit {
  public ModelAndView render(Request request, Response response) {
    Integer idUsuario = null;

    try {
      Object userIdObject = request.session().attribute("user_id");

      if (!(userIdObject instanceof Integer)) {
        throw new IllegalStateException("Sesión inválida");
      }

      idUsuario = (Integer) userIdObject;
    } catch (Exception e) {
      response.redirect("/login");
      return null;
    }

    Usuario usuarioLogueado = RepositorioUsuarios.getInstance().porId(idUsuario);

    Map<String, Object> modelo = new HashMap<>();

    var comunidades = RepositorioComunidades.getInstance().comunidadesDeUsuario(usuarioLogueado);
    var incidentes = RepositorioIncidentes.getInstance().incidentesARevisarPara(usuarioLogueado);

    modelo.put("comunidades", comunidades);
    modelo.put("incidentes", formatearIncidentes(incidentes, comunidades));

    return new ModelAndView(modelo, "index.html.hbs");
  }

  private List<Map<String, Object>> formatearIncidentes(List<Incidente> incidentes,
                                                        List<Comunidad> comunidades) {
    List<Map<String, Object>> results = new ArrayList<>();

    for(Incidente incidente : incidentes) {
      var comunidad = comunidades.stream()
          .filter(c -> c.getIncidentes().contains(incidente))
          .findFirst()
          .orElse(null);
          results.add(new HashMap<>() {{
            put("id", incidente.getId());
            put("servicio", incidente.getServicio());
            put("establecimiento", incidente.getEstablecimiento());
            put("fecha", incidente.getFecha());
            put("reportante", incidente.getReportante());
            put("abierto", !incidente.estaResuelto());
            put("observaciones", incidente.getObservaciones());
            put("comunidad_id", comunidad.getId());
          }});
    };

    return results;
  }
}
