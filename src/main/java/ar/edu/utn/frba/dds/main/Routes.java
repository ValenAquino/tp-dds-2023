package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controller.*;
import ar.edu.utn.frba.dds.controller.response.ApiResponse;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import com.google.gson.Gson;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.PersistenceException;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.*;

public class Routes implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    Bootstrap.main(args);
    new Routes().start();
  }

  public void start() {
    try {
      port(Integer.parseInt(System.getenv("PORT")));
    } catch (NumberFormatException e) {
      port(8555);
    }

    staticFileLocation("/public");

    var engine = new HandlebarsTemplateEngine();
    var landingController = new LandingController();
    var homeController = new HomeController();
    var sessionController = new SessionController();
    var comunidadesController = new ComunidadesController();
    var usuariosController = new UsuariosController();
    var incidentesController = new IncidentesController();
    var rankingsController = new RankingsController();
    var serviciosController = new ServiciosController();
    var apiController = new ApiController();

    // Anonymous
    get("/", landingController::render, engine);
    get("/login", sessionController::render, engine);
    post("/login", sessionController::login);
    post("/logout", sessionController::logout);

    // Protected "home" routes
    get("/home", homeController::render, engine);

    // Perfil
    get("/perfil", usuariosController::perfil, engine);
    put("/perfil", usuariosController::editarPerfil, engine);

    // Usuarios
    get("/usuarios", usuariosController::usuarios, engine);
    get("/usuarios/nuevo", usuariosController::nuevo, engine);
    post("/usuarios", usuariosController::crear);
    get("/usuarios/:id", usuariosController::ver, engine);
    put("/usuarios/editar", usuariosController::editar);
    delete("/usuarios/:id", usuariosController::eliminar);

    // Comunidades
    get("/comunidades", comunidadesController::listar, engine);
    delete("/comunidades/:id", comunidadesController::eliminar);
    // get("/comunidades/:editar", comunidadesController::editar);
    get("/comunidades/:id/incidentes", incidentesController::listarPorComunidad, engine);
    delete("/comunidades/:id/incidentes/:incidente_id", incidentesController::cerrar);

    // Servicios
    get("/servicios", serviciosController::listar, engine);

    // Incidentes
    get("/incidentes/nuevo", incidentesController::nuevo, engine);
    post("/incidentes", incidentesController::reportarIncidente);
    get("/incidentes", incidentesController::listarPendientes, engine);

    // Rankings
    get("/rankings/cantidad-incidentes", rankingsController::renderCantidadIncidentes, engine);
    get("/rankings/promedio-cierre", rankingsController::renderMayorPromedioCierre, engine);
    post("/rankings/cantidad-incidentes", rankingsController::exportarCantidadIncidentes);
    post("/rankings/promedio-cierre", rankingsController::exportarMayorPromedioCierre);

    // Api
    get("/api/comunidades", "application/json", apiController::listarComunidades);
    get("/api/comunidades/:id", "application/json", apiController::detalleComunidad);
    post("/api/comunidades", "application/json", apiController::crearComunidad);
    patch("/api/comunidades/:id", "application/json", apiController::editarComunidad);
    delete("/api/comunidades/:id", "application/json", apiController::eliminarComunidad);

    // Filtros
    before((request, response) -> entityManager().clear());
    before("/login", Routes::evaluarAutenticacion);
    before("/*", Routes::evaluarNoAutenticacion);
    before("/api/*", Routes::evaluarBasicAuth);

    before("/usuarios", Routes::confirmarRolAdmin);
    before("/usuarios/*", Routes::confirmarRolAdmin);
    before("/rankings/*", Routes::confirmarRolAdmin);

    after((request, response) -> {
      response.header("Cache-Control", "no-store, no-cache, must-revalidate");
      response.header("Pragma", "no-cache");
      response.header("Expires", "0");
    });

    // Excepciones
    exception(PersistenceException.class, (e, request, response) -> response.redirect("/500"));
  }

  private static void confirmarRolAdmin(Request request, Response response) {
    String is_admin = request.session().attribute("is_admin").toString();

    if (!Boolean.parseBoolean(is_admin)) {
      response.redirect("/home");
    }
  }

  private static void evaluarBasicAuth(Request request, Response response) {
    response.type("application/json");
    String authHeader = request.headers("Authorization");
    var gson = new Gson();

    if (authHeader == null || !authHeader.startsWith("Basic ")) {
      halt(401, gson.toJson(new ApiResponse(false, "No tiene permiso para acceder al recurso", null)));
    }

    String credentials = new String(java.util.Base64.getDecoder().decode(authHeader.substring(6)));
    String[] parts = credentials.split(":");

    if (parts.length != 2 || !credencialesSonValidas(parts[0], parts[1])) {
      halt(401, gson.toJson(new ApiResponse(false, "No tiene permiso para acceder al recurso", null)));
    }
  }

  private static void evaluarAutenticacion(Request request, Response response) {
    if (request.session().attribute("user_id") != null) {
      response.redirect("/home");
    }
  }

  private static void evaluarNoAutenticacion(Request request, Response response) {
    if (!request.pathInfo().equals("/") && //avoid landing
        !request.pathInfo().matches("/[^/]+\\.[^/]+") && //avoid static files
        !request.pathInfo().matches("/login(?:\\\\?.*)?") && //avoid login routes
        !request.pathInfo().matches("/api.+") && //avoid api
        request.pathInfo().matches("/.+")) {
      if (request.session().attribute("user_id") == null) {
        response.redirect("/login?origin=" + request.pathInfo());
      }
    }
  }

  private static boolean credencialesSonValidas(String username, String contrasenia) {
    Usuario usuario = RepositorioUsuarios.getInstance().porUsuarioYContrasenia(username, contrasenia);
    return usuario != null && usuario.esAdmin();
  }
}
