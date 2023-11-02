package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class HomeController implements WithSimplePersistenceUnit {
  public ModelAndView render(Request request, Response response) {
    var idUsuario = request.session().attribute("user_id");
    var usuarioLogueado = RepositorioUsuarios.getInstance().porId((Integer) idUsuario);

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("comunidades", RepositorioComunidades.getInstance().comunidadesDeUsuario(usuarioLogueado));
    return new ModelAndView(modelo, "index.html.hbs");
  }
}
