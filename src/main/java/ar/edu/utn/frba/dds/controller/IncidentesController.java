package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Comunidad;
import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioServicios;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class IncidentesController implements WithSimplePersistenceUnit {
  public ModelAndView listarPorComunidad(Request request, Response response) {
    var idComunidad = Integer.valueOf(request.params("id"));

    var estado = request.queryParams("estado");
    var comunidad = RepositorioComunidades.getInstance().porId(idComunidad);

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("estado", estado == null ? "todos" : estado);
    modelo.put("incidentes",  getIncidentesPorEstado(comunidad, estado));
    modelo.put("comunidad_id", Integer.valueOf(request.params("id")));
    modelo.put("comunidad_nombre", comunidad.getNombre());
    modelo.put("es_admin", request.attribute("es_admin"));
    return new ModelAndView(modelo, "pages/incidentes.html.hbs");
  }

  public ModelAndView listarPendientes(Request request, Response response) {
    Usuario usuarioLogueado = SessionController.usuarioLogueado(request);

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("incidentes",  formatearIncidentes(RepositorioIncidentes.getInstance().incidentesARevisarPara(usuarioLogueado)));
    modelo.put("vista", "pendientes");
    return new ModelAndView(modelo, "pages/incidentes.html.hbs");
  }

  public ModelAndView cerrar(Request request, Response response) {
    withTransaction(() -> {
      Usuario usuarioLogueado = SessionController.usuarioLogueado(request);

      var idComunidad = Integer.valueOf(request.params("id"));

      var incidente = RepositorioIncidentes.getInstance()
          .porId(Integer.valueOf(request.params("incidente_id")));

      var comunidad = RepositorioComunidades.getInstance()
          .porId(idComunidad);

      usuarioLogueado.cerrarIncidente(comunidad, incidente, LocalDateTime.now());

      RepositorioIncidentes.getInstance().persistir(incidente);

      var from = request.queryParams("from");

      if (from.equals("index")) {
        response.redirect("/home?after_action=true&message=Incidente%20cerrado%20con%20%C3%A9xito");
      } else if (from.equals("pendientes")) {
        response.redirect("/incidentes?after_action=true");
      }
      else
        response.redirect("/comunidades/" + idComunidad + "/incidentes");
    });

    return null;
  }

  public ModelAndView nuevo(Request request, Response response) {
    Map<String, Object> model = new HashMap<>();
    model.put("servicios", RepositorioServicios.getInstance().todos());
    model.put("incidentes", RepositorioIncidentes.getInstance().todos());
    model.put("es_admin", request.attribute("es_admin"));
    return new ModelAndView(model, "incidentes/reportarIncidente.html.hbs");
  }

  public Void reportarIncidente(Request request, Response response) {
    withTransaction(() -> {
      Servicio servicio = RepositorioServicios.getInstance().porId(Integer.parseInt(request.queryParams("servicio")));
      Usuario usuario = SessionController.usuarioLogueado(request);
      usuario.reportarIncidente(servicio, LocalDateTime.now(), request.queryParams("observaciones"));
      RepositorioUsuarios.getInstance().persistir(usuario);
      var from = request.queryParams("from");

      if (from != null && from.equals("servicios")){
        request.session().attribute("reporte_exitoso", Boolean.TRUE);
        response.redirect("/servicios");
      }
      else
        response.redirect("/home");
    });
    return null;
  }

  private List<Incidente> getIncidentesPorEstado(Comunidad comunidad, String estado) {
    if (estado == null)
      return comunidad.getIncidentes();

    if (estado.equals("abierto"))
      return comunidad.getIncidentesAbiertos();
    else
      return comunidad.getIncidentesResueltos();
  }

  private List<Map<String, Object>> formatearIncidentes(List<Incidente> incidentes) {
    List<Map<String, Object>> results = new ArrayList<>();

    var comunidades = RepositorioComunidades.getInstance().todas();

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
}
