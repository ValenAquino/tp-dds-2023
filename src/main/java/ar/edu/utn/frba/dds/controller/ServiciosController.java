package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioServicios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ServiciosController implements WithSimplePersistenceUnit {
  public ModelAndView listar(Request request, Response response) {
    Usuario usuarioLogueado = SessionController.usuarioLogueado(request);
    var servicios = RepositorioServicios.getInstance().porUsuario(usuarioLogueado);

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("servicios", servicios);
    return new ModelAndView(modelo, "pages/servicios.html.hbs");
  }
}
