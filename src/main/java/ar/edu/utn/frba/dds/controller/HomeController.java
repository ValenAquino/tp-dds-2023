package ar.edu.utn.frba.dds.controller;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class HomeController implements WithSimplePersistenceUnit  {
  public ModelAndView list(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    return new ModelAndView(modelo, "index.html.hbs");
  }
}
