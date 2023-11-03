package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;

public class UsuariosController implements WithSimplePersistenceUnit {

  public ModelAndView usuarios(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("usuarios", RepositorioUsuarios.getInstance().todos());
    return new ModelAndView(modelo, "usuarios/usuarios.html.hbs");
  }

  public ModelAndView nuevo(Request request, Response response) {
    return new ModelAndView(null, "usuarios/nuevo.html.hbs");
  }

  public Void crear(Request request, Response response) {
    withTransaction(() -> {

      var usuarioNuevo = new Usuario(
          request.queryParams("usuario"),
          request.queryParams("contrasenia"),
          request.queryParams("nombre"),
          request.queryParams("apellido"),
          request.queryParams("correo_electronico")
      );
      usuarioNuevo.setAdmin(request.queryParams("es_admin") != null);

      RepositorioUsuarios.getInstance().persistir(usuarioNuevo);
    });

    response.redirect("/home/usuarios");
    return null;
  }

  public ModelAndView ver(Request request, Response response) {
    Usuario usuario = RepositorioUsuarios.getInstance()
        .porId(Integer.parseInt(request.params("id")));
    return new ModelAndView(usuario, "usuarios/usuario.html.hbs");
  }

  public Void editar(Request request, Response response) {
    withTransaction(() ->
        RepositorioUsuarios.getInstance().persistir(new Usuario(
            // TODO
        )));
    response.redirect("/usuarios");
    return null;
  }

}
