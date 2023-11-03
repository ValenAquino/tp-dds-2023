package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controller.*;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.PersistenceException;
import spark.Spark;
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
    port(8555);
    staticFileLocation("/public");

    var engine = new HandlebarsTemplateEngine();
    var homeController = new HomeController();
    var sessionController = new SessionController();
    var comunidadesController = new ComunidadesController();
    var usuariosController = new UsuariosController();
    var incidentesController = new IncidentesController();
    var rankingsController = new RankingsController();
    var serviciosController = new ServiciosController();

    // Anonymous
    get("/login", sessionController::render, engine);
    post("/login", sessionController::login);
    post("/logout", sessionController::logout);

    // Protected "home" routes
    get("/home", homeController::render, engine);

    // Usuarios
    get("/home/usuarios", usuariosController::usuarios, engine);
    get("/home/usuarios/nuevo", usuariosController::nuevo, engine);
    post("/home/usuarios", usuariosController::crear);
    get("/home/usuarios/:id", usuariosController::ver, engine);
    post("/home/usuarios/editar", usuariosController::editar);
    post("/home/usuarios/eliminar", usuariosController::eliminar);

    // Comunidades
    get("/home/comunidades", comunidadesController::listar, engine);
    post("/home/comunidades/:id/eliminar", comunidadesController::eliminar);
    get("/home/comunidades/:id/incidentes", incidentesController::listarPorComunidad, engine);
    post("/home/comunidades/:id/incidentes/:incidente_id", incidentesController::cerrar);

    // Servicios
    get("/home/servicios", serviciosController::listar, engine);

    // Incidentes
    Spark.get("/home/incidentes/nuevo", incidentesController::nuevo, engine);
    Spark.post("/home/incidentes/nuevo", incidentesController::reportarIncidente);

    // Rankings
    get("/home/rankings/cantidad-incidentes", rankingsController::renderCantidadIncidentes, engine);
    get("/home/rankings/promedio-cierre", rankingsController::renderMayorPromedioCierre, engine);
    post("/home/rankings/cantidad-incidentes", rankingsController::exportarCantidadIncidentes);
    post("/home/rankings/promedio-cierre", rankingsController::exportarMayorPromedioCierre);

    // Filtros
    before("/", (request, response) -> response.redirect("/home"));
    before((request, response) -> entityManager().clear());
    before("/home", Routes::evaluarNoAutenticacion);
    before("/home/*", Routes::evaluarNoAutenticacion);
    before("/login", Routes::evaluarAutenticacion);

    // Excepciones
    exception(PersistenceException.class, (e, request, response) -> response.redirect("/500"));
  }

  private static void evaluarAutenticacion(Request request, Response response) {
    if (request.session().attribute("user_id") != null) {
      response.redirect("/home");
    }
  }

  private static void evaluarNoAutenticacion(Request request, Response response) {
    if (request.session().attribute("user_id") == null) {
      response.redirect("/login?origin=" + request.pathInfo());
    }
  }
}
