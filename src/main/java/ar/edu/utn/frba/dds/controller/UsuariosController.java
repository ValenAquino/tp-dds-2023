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
    usuario.vaciarContrasenia();
    return new ModelAndView(usuario, "usuarios/usuario.html.hbs");
  }

  public Void editar(Request request, Response response) {
    withTransaction(() -> {

      var usuario = RepositorioUsuarios.getInstance().porId(
          Integer.parseInt(request.queryParams("id"))
      );

      usuario.setUsuario(request.queryParams("usuario"));
      if (!request.queryParams("contrasenia").isBlank()) {
        usuario.setContrasenia(request.queryParams("contrasenia"));
      }
      usuario.setNombre(request.queryParams("nombre"));
      usuario.setApellido(request.queryParams("apellido"));
      usuario.setCorreoElectronico(request.queryParams("correo_electronico"));
      usuario.setAdmin(request.queryParams("es_admin") != null);

      RepositorioUsuarios.getInstance().persistir(usuario);
    });

    response.redirect("/home/usuarios");
    return null;
  }

}
