package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controller.HomeController;
import ar.edu.utn.frba.dds.controller.IncidentesController;
import ar.edu.utn.frba.dds.controller.SessionController;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.PersistenceException;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Routes implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    Bootstrap.main(args);
    new Routes().start();
  }

  public void start() {
    System.out.println("Iniciando servidor");

    Spark.port(8555);
    Spark.staticFileLocation("/public");

    var engine = new HandlebarsTemplateEngine();
    var homeController = new HomeController();
    var sesionController = new SessionController();
    var incidentesController = new IncidentesController();

    Spark.get("/", homeController::render, engine);

    // Login routes
    Spark.get("/login", sesionController::render, engine);
    Spark.post("/login", sesionController::login);

    // Incidentes routes
    Spark.get("/incidentes", incidentesController::render, engine);

    Spark.exception(PersistenceException.class, (e, request, response) -> {
      response.redirect("/500");
    });

    Spark.before((request, response) -> {
      entityManager().clear();
    });
  }

}
