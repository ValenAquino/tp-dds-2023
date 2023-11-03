package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controller.ComunidadesController;
import ar.edu.utn.frba.dds.controller.HomeController;
import ar.edu.utn.frba.dds.controller.IncidentesController;
import ar.edu.utn.frba.dds.controller.SessionController;
import ar.edu.utn.frba.dds.controller.UsuariosController;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.PersistenceException;

import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
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
    var usuariosController = new UsuariosController();
    var incidentesController = new IncidentesController();

    // Anonymous
    get("/login", sessionController::render, engine);
    post("/login", sessionController::login);
    post("/logout", sessionController::logout);

    // Users routes
    get("/home/usuarios", usuariosController::usuarios, engine);
    get("/home/usuarios/nuevo", usuariosController::nuevo, engine);
    post("/home/usuarios", usuariosController::crear);
    get("/home/usuarios/:id", usuariosController::ver, engine);
    post("/home/usuarios/editar", usuariosController::editar);

    // Protected "home" routes
    get("/home", homeController::render, engine);
    // --> Comunidades
    get("/home/comunidades", comunidadesController::listar, engine);
    get("/home/comunidades/:id/incidentes", incidentesController::listarPorComunidad, engine);
    post("/home/comunidades/:id/incidentes/:incidente_id", incidentesController::cerrar);

    exception(PersistenceException.class, (e, request, response) -> {
      response.redirect("/500");
    });

    before((request, response) -> {
      entityManager().clear();
    });

    before("/", (request, response) -> {
      response.redirect("/home");
    });

    before("/login", (request, response) -> {
      if (request.session().attribute("user_id") != null) {
        response.redirect("/home");
      }
    });

    before("/home", Routes::evaluarAutenticacion);

    before("/home/*", Routes::evaluarAutenticacion);
  }

  private static void evaluarAutenticacion(Request request, Response response) {
    var path = request.pathInfo();
    if (request.session().attribute("user_id") == null) {
      response.redirect("/login?origin=" + path);
    }
  }

}
