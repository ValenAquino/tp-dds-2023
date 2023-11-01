package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;

public class UsuariosController implements WithSimplePersistenceUnit  {
  public ModelAndView render(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("usuarios", RepositorioUsuarios.getInstance().todos());
    return new ModelAndView(modelo, "usuarios/usuarios.html.hbs");
  }
}
