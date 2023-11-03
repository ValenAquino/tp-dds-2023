package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Comunidad;
import ar.edu.utn.frba.dds.model.entidades.Establecimiento;
import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioEntidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioServicios;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncidentesController implements WithSimplePersistenceUnit {
  public ModelAndView listarPorComunidad(Request request, Response response) {
    var idComunidad = Integer.valueOf(request.params("id"));

    var estado = request.queryParams("estado");

    var incidentes =
        getIncidentesPorEstado(idComunidad, estado);

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("incidentes", formatearIncidentes(incidentes));
    modelo.put("comunidad_id", Integer.valueOf(request.params("id")));
    return new ModelAndView(modelo, "pages/incidentes/incidentes.html.hbs");
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

  private List<Map<String, Object>> formatearIncidentes(List<Incidente> incidentes) {
    List<Map<String, Object>> results = new ArrayList<>();

    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    incidentes.forEach(incidente ->
        results.add(new HashMap<>() {{
          put("id", incidente.getId());
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

  public ModelAndView nuevo(Request request, Response response) {
//    Usuario usuarioLogueado = SessionController.usuarioLogueado(request);
//    List<Comunidad> comunidadesDelUsuario = RepositorioComunidades.getInstance().comunidadesDeUsuario(usuarioLogueado);
    Map<String, Object> model = new HashMap<>();
//    List<Servicio> serviciosDeInteres = comunidadesDelUsuario
//        .stream()
//        .flatMap(comunidad -> comunidad.getServiciosDeInteres().stream())
//        .toList();
    model.put("servicios", RepositorioServicios.getInstance().todos());
    model.put("incidentes", RepositorioIncidentes.getInstance().todos());
    return new ModelAndView(model, "incidentes/reportarIncidente.html.hbs");
  }

  public Void reportarIncidente(Request request, Response response) {
    Servicio servicio = RepositorioServicios.getInstance().porId(Integer.parseInt(request.queryParams("servicio")));
    Usuario usuario = SessionController.usuarioLogueado(request);
    usuario.reportarIncidente(servicio,LocalDateTime.now(),request.queryParams("observaciones"));
    RepositorioUsuarios.getInstance().persistir(usuario);
    response.redirect("/home");
    return null;
  }

}
