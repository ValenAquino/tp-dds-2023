package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioServicios;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncidentesController implements WithSimplePersistenceUnit {
  public ModelAndView listarPorComunidad(Request request, Response response) {
    var idComunidad = Integer.valueOf(request.params("id"));

    var estado = request.queryParams("estado");

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("incidentes",  getIncidentesPorEstado(idComunidad, estado));
    modelo.put("comunidad_id", Integer.valueOf(request.params("id")));
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
      }
      else
        response.redirect("/home/comunidades/" + idComunidad + "/incidentes");
    });

    return null;
  }

  private List<Incidente> getIncidentesPorEstado(Integer idComunidad, String estado) {
    var comunidad = RepositorioComunidades.getInstance().porId(idComunidad);

    if (estado == null)
      return comunidad.getIncidentes();

    if (estado.equals("abierto"))
      return comunidad.getIncidentesAbiertos();
    else
      return comunidad.getIncidentesResueltos();
  }
  public ModelAndView nuevo(Request request, Response response) {
    Map<String, Object> model = new HashMap<>();
    model.put("servicios", RepositorioServicios.getInstance().todos());
    model.put("incidentes", RepositorioIncidentes.getInstance().todos());
    return new ModelAndView(model, "incidentes/reportarIncidente.html.hbs");
  }

  public Void reportarIncidente(Request request, Response response) {
    withTransaction(() -> {
      Servicio servicio = RepositorioServicios.getInstance().porId(Integer.parseInt(request.queryParams("servicio")));
      Usuario usuario = SessionController.usuarioLogueado(request);
      usuario.reportarIncidente(servicio, LocalDateTime.now(), request.queryParams("observaciones"));
      RepositorioUsuarios.getInstance().persistir(usuario);

      var from = request.queryParams("from");

      if (from != null && from.equals("servicios"))
        response.redirect("/home/servicios");
      else
        response.redirect("/home");
    });
    return null;
  }
}
