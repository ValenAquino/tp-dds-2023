package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class SessionController {
  // TODO GRAN TODO: notar que las responsabildades
  // de saber si una personas está con sesión inciada,
  // de saber le usuarie actual, etc, probablmente se vayan a repetir
  // y convendrá generalizarlas

  public ModelAndView render(Request request, Response response) {

    Map<String, Object> modelo = new HashMap<>();
    return new ModelAndView(modelo, "login.html.hbs");
  }

  public Void login(Request request, Response response) {
    try {
      Usuario usuario = RepositorioUsuarios.getInstance().porUsuarioYContrasenia(
          request.queryParams("usuario"),
          request.queryParams("contrasenia"));

      request.session().attribute("user_id", usuario.getId());
      // TODO: Redirect to intended route
      response.redirect("/");
      return null;
    } catch (Exception e) {
      response.redirect("/login");
      return null;
    }
  }
}
