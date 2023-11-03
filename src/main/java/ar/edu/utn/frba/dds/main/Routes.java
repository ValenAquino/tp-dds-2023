package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controller.*;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
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
    var homeController = new HomeController();
    var sessionController = new SessionController();
    var comunidadesController = new ComunidadesController();
    var incidentesController = new IncidentesController();
    var rankingsController = new RankingsController();

    // Anonymous
    get("/login", sessionController::render, engine);
    post("/login", sessionController::login);
    post("/logout", sessionController::logout);

    // Protected "home" routes
    get("/home", homeController::render, engine);
    // --> Comunidades
    get("/home/comunidades", comunidadesController::listar, engine);
    get("/home/comunidades/:id/incidentes", incidentesController::listarPorComunidad, engine);
    post("/home/comunidades/:id/incidentes/:incidente_id", incidentesController::cerrar);
    // --> Rankings
    get("/home/rankings/cantidad-incidentes", rankingsController::renderCantidadIncidentes, engine);
    get("/home/rankings/promedio-cierre", rankingsController::renderMayorPromedioCierre, engine);
    post("/home/rankings/cantidad-incidentes", rankingsController::exportarCantidadIncidentes);
    post("/home/rankings/promedio-cierre", rankingsController::exportarMayorPromedioCierre);

    before("/", (request, response) -> {
      response.redirect("/home");
    });

    before("/login", (request, response) -> {
      if (request.session().attribute("user_id") != null) {
        // TODO: Redirect to previous path
        response.redirect("/home");
      }
    });

    // Have to repeat logic since `/home` is not matched by `home/*`
    before("/home", (request, response) -> {
      var path = request.pathInfo();
      if (request.session().attribute("user_id") == null) {
        response.redirect("/login?origin=" + path);
      }
    });

    before("/home/*", (request, response) -> {
      var path = request.pathInfo();
      if (request.session().attribute("user_id") == null) {
        response.redirect("/login?origin=" + path);
      }
    });
  }

}
