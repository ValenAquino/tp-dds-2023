package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class UsuariosController implements WithSimplePersistenceUnit {

  public ModelAndView usuarios(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    Boolean creacionExitosa = request.session().attribute("creacion_exitosa");
    modelo.put("es_admin", request.attribute("es_admin"));
    modelo.put("usuarios", RepositorioUsuarios.getInstance().todos());
    if(creacionExitosa!=null){
      request.session().removeAttribute("creacion_exitosa");
      modelo.put("creacion_exitosa",creacionExitosa);
    }
    return new ModelAndView(modelo, "pages/usuarios.html.hbs");
  }

  public ModelAndView nuevo(Request request, Response response) {
    return new ModelAndView(null, "usuarios/nuevo.html.hbs");
  }

  public Void crear(Request request, Response response) {
    AtomicBoolean exito = new AtomicBoolean(false);
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
      exito.set(true);
    });
    request.session().attribute("creacion_exitosa", Boolean.TRUE);
    response.redirect("/usuarios");
    return null;
  }


  public ModelAndView ver(Request request, Response response) {
    Usuario usuario = RepositorioUsuarios.getInstance()
        .porId(Integer.parseInt(request.params("id")));
    usuario.vaciarContrasenia();

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("es_admin", request.attribute("es_admin"));
    modelo.put("usuario", usuario);

    return new ModelAndView(modelo, "usuarios/usuario.html.hbs");
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

    response.redirect("/usuarios");
    return null;
  }

  public Void eliminar(Request request, Response response) {
    withTransaction(() -> {
      var usuarioId = Integer.parseInt(request.queryParams("id"));
      var usuario = RepositorioUsuarios.getInstance().porId(usuarioId);

      RepositorioComunidades.getInstance().comunidadesDeUsuario(usuario)
          .forEach(comunidad -> comunidad.eliminarMiembro(usuario));

      RepositorioIncidentes.getInstance().incidentesDelReportante(usuario)
          .forEach(incidente -> incidente.setReportante(null));

      RepositorioUsuarios.getInstance().eliminar(usuario);
    });

    response.redirect("/usuarios");
    return null;
  }
}
