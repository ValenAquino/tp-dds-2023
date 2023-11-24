package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.CustomModel;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class LandingController {
  public ModelAndView render(Request request, Response response) {
    Map<String, Object> modelo = new CustomModel("ServiceWatch", request);
    return new ModelAndView(modelo, "landing/landing.html.hbs");
  }
}
