package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controller.HomeController;
import ar.edu.utn.frba.dds.controller.IncidentesController;
import ar.edu.utn.frba.dds.controller.SessionController;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
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

    HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
    HomeController homeController = new HomeController();
    IncidentesController incidentesController = new IncidentesController();
    var sesionController = new SessionController();

    Spark.get("/", homeController::render, engine);

    // Login routes
    Spark.get("/login", sesionController::render, engine);
    Spark.post("/login", sesionController::login);

    // Incidentes routes
    Spark.get("/incidentes/nuevo", incidentesController::nuevo, engine);  // Devolver el formulario vacío
    Spark.post("/incidentes/nuevo", incidentesController::reportarIncidente, engine); // Enviar formulario

    Spark.exception(PersistenceException.class, (e, request, response) -> {
      response.redirect("/500");
    });

    Spark.before((request, response) -> {
      entityManager().clear();
    });
  }

}
