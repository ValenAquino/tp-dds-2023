package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controller.*;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.PersistenceException;
import spark.Spark;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import javax.persistence.PersistenceException;

import static spark.Spark.*;

public class Routes implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    Bootstrap.main(args);
    new Routes().start();
  }

  public void start() {
    port(8555);
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

    // Anonymous
    get("/", landingController::render, engine);
    get("/login", sessionController::render, engine);
    post("/login", sessionController::login);
    post("/logout", sessionController::logout);

    // Protected "home" routes
    get("/home", homeController::render, engine);

    // Usuarios
    get("/usuarios", usuariosController::usuarios, engine);
    get("/usuarios/nuevo", usuariosController::nuevo, engine);
    post("/usuarios", usuariosController::crear);
    get("/usuarios/:id", usuariosController::ver, engine);
    post("/usuarios/editar", usuariosController::editar);
    post("/usuarios/eliminar", usuariosController::eliminar);

    // Comunidades
    get("/comunidades", comunidadesController::listar, engine);
    post("/comunidades/eliminar", comunidadesController::eliminar);
    get("/comunidades/:id/incidentes", incidentesController::listarPorComunidad, engine);
    post("/comunidades/:id/incidentes/:incidente_id", incidentesController::cerrar);

    // Servicios
    get("/servicios", serviciosController::listar, engine);

    // Incidentes
    Spark.get("/incidentes/nuevo", incidentesController::nuevo, engine);
    Spark.post("/incidentes", incidentesController::reportarIncidente);
    get("/incidentes", incidentesController::listarPendientes, engine);

    // Rankings
    get("/rankings/cantidad-incidentes", rankingsController::renderCantidadIncidentes, engine);
    get("/rankings/promedio-cierre", rankingsController::renderMayorPromedioCierre, engine);
    post("/rankings/cantidad-incidentes", rankingsController::exportarCantidadIncidentes);
    post("/rankings/promedio-cierre", rankingsController::exportarMayorPromedioCierre);

    // Filtros
    before((request, response) -> entityManager().clear());
    before("/login", Routes::evaluarAutenticacion);
    before("/*", Routes::evaluarNoAutenticacion);

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
    if (!SessionController.esAdmin(request)) {
      response.redirect("/home");
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
        request.pathInfo().matches("/.+")) {
      if (request.session().attribute("user_id") == null) {
        response.redirect("/login?origin=" + request.pathInfo());
      } else {
          request.attribute("es_admin", SessionController.esAdmin(request));
      }
    }
  }
}
