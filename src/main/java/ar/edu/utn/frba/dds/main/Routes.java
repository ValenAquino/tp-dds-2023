package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controller.ComunidadesController;
import ar.edu.utn.frba.dds.controller.HomeController;
import ar.edu.utn.frba.dds.controller.IncidentesController;
import ar.edu.utn.frba.dds.controller.SessionController;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.PersistenceException;
import spark.Spark;

import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

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

    // Incidentes routes
    Spark.get("/incidentes/nuevo", incidentesController::nuevo, engine);  // Devolver el formulario vacío
    Spark.post("/incidentes/nuevo", incidentesController::reportarIncidente, engine); // Enviar formulario

    Spark.exception(PersistenceException.class, (e, request, response) -> {
      response.redirect("/500");
    });

    exception(PersistenceException.class, (e, request, response) -> {
      response.redirect("/500");
    });
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
