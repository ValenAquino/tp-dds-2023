package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class SessionController {

  public ModelAndView render(Request request, Response response) {
    String mensajeError = request.session().attribute("mensaje_error");
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("origin", request.queryParams("origin"));
    if (mensajeError != null) {
      request.session().removeAttribute("mensaje_error");
      modelo.put("mensaje_error", mensajeError);
    }
    return new ModelAndView(modelo, "login.html.hbs");
  }

  public Void login(Request request, Response response) {
    try {
      Usuario usuario = RepositorioUsuarios.getInstance().porUsuarioYContrasenia(
          request.queryParams("usuario"),
          request.queryParams("contrasenia"));

      String origin = request.queryParams("origin");
      request.session().attribute("user_id", usuario.getId());

      if (origin != null && !origin.isBlank()) {
        response.redirect(origin);
      } else {
        response.redirect("/home");
      }
      return null;
    } catch (Exception e) {
      request.session().attribute("mensaje_error", "Usuario o contraseña incorrectos");
      response.redirect("/login");
      return null;
    }
  }

  public Void logout(Request request, Response response) {
    request.session().removeAttribute("user_id");
    response.redirect("/login");
    return null;
  }

  public static Usuario usuarioLogueado(Request request) {
    var idUsuario = request.session().attribute("user_id");
    return RepositorioUsuarios.getInstance().porId((Integer) idUsuario);
  }

  public static boolean esAdmin(Request request) {
    return usuarioLogueado(request).esAdmin();
  }
}
