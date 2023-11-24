package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Comunidad;
import ar.edu.utn.frba.dds.model.entidades.CustomModel;
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
    Integer idUsuario = request.session().attribute("user_id");
    Boolean afterAction = request.session().attribute("after_action");
    String message = request.session().attribute("message");

    request.session().removeAttribute("after_action");
    request.session().removeAttribute("message");

    Usuario usuarioLogueado = RepositorioUsuarios.getInstance().porId(idUsuario);

    Map<String, Object> modelo = new CustomModel("Home", request);

    var comunidades = RepositorioComunidades.getInstance().comunidadesDeUsuario(usuarioLogueado, 3);
    var incidentes = RepositorioIncidentes.getInstance().incidentesARevisarPara(usuarioLogueado, 3);

    modelo.put("comunidades", formatearComunidades(comunidades));
    modelo.put("incidentes", formatearIncidentes(incidentes, comunidades));
    modelo.put("after_action", afterAction);
    modelo.put("message", message);
    modelo.put("total_comunidades", comunidades.size());
    modelo.put("total_incidentes", incidentes.size());

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
            put("comunidad", comunidad);
          }});
    };

    return results;
  }

  private List<Map<String, Object>> formatearComunidades(List<Comunidad> comunidades) {
    List<Map<String, Object>> results = new ArrayList<>();

    for(Comunidad comunidad : comunidades) {
      results.add(new HashMap<>() {{
        put("id", comunidad.getId());
        put("nombre", comunidad.getNombre());
        put("cantidad_miembros", comunidad.getMiembros().size());
        put("cantidad_incidentes", comunidad.getIncidentesAbiertos().size());
        put("miembros_de_comunidad", comunidad.getMiembros());
      }});
    };

    return results;
  }
}
