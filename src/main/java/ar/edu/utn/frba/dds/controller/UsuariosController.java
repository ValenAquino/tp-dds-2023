package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.CustomModel;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import ar.edu.utn.frba.dds.model.excepciones.ValidacionContrasenaException;
import ar.edu.utn.frba.dds.model.password.politicas.PoliticaLongitud;
import ar.edu.utn.frba.dds.model.password.politicas.PoliticaRegex;
import ar.edu.utn.frba.dds.model.password.validacion.ValidadorContrasena;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class UsuariosController implements WithSimplePersistenceUnit {

  public ModelAndView usuarios(Request request, Response response) {
    Map<String, Object> modelo = new CustomModel("Usuarios", request);
    modelo.put("usuarios", RepositorioUsuarios.getInstance().todos());
    modelo.put("mensajeExito", request.session().attribute("mensajeExito"));
    modelo.put("logged_user_id", request.session().attribute("user_id"));

    request.session().removeAttribute("mensajeExito");

    return new ModelAndView(modelo, "pages/usuarios.html.hbs");
  }

  public ModelAndView nuevo(Request request, Response response) {
    Map<String, Object> modelo = new CustomModel("Crear usuario", request);
    modelo.put("mensajeError", request.session().attribute("mensajeError"));
    modelo.put("usuario", request.session().attribute("usuario"));

    request.session().removeAttribute("mensajeError");
    request.session().removeAttribute("usuario");

    return new ModelAndView(modelo, "usuarios/nuevo.html.hbs");
  }

  public ModelAndView crear(Request request, Response response) {
    var usuarioNuevo = getUsuarioDeRequest(request);

    try {
      validarUsuario(usuarioNuevo);
      validarContrasenia(usuarioNuevo.getContrasenia());
      withTransaction(() -> {
        RepositorioUsuarios.getInstance().persistir(usuarioNuevo);
        request.session().attribute(
            "mensajeExito",
            "El usuario ha sido creado con éxito.");
      });
      response.redirect("/usuarios");
    } catch (Exception e) {
      usuarioNuevo.vaciarContrasenia();
      request.session().attribute("mensajeError", e.getMessage());
      request.session().attribute("usuario", usuarioNuevo);
      response.redirect("/usuarios/nuevo");
    }

    return null;
  }

  public ModelAndView ver(Request request, Response response) {
    Usuario usuario = request.session().attribute("usuario") != null
        ? request.session().attribute("usuario")
        : RepositorioUsuarios.getInstance().porId(Integer.parseInt(request.params("id")));

    usuario.vaciarContrasenia();

    Map<String, Object> modelo = new CustomModel("Editar usuario", request);
    modelo.put("es_admin", request.session().attribute("is_admin"));
    modelo.put("mensajeError", request.session().attribute("mensajeError"));
    modelo.put("usuario", usuario);

    request.session().removeAttribute("mensajeError");
    request.session().removeAttribute("usuario");

    return new ModelAndView(modelo, "usuarios/usuario.html.hbs");
  }

  public ModelAndView editar(Request request, Response response) {
    var usuarioExistente = getUsuarioDeRequest(request);

    try {
      validarUsuario(usuarioExistente);
      if (!request.queryParams("contrasenia").isBlank()) {
        validarContrasenia(usuarioExistente.getContrasenia());
      }
      withTransaction(() -> {
        RepositorioUsuarios.getInstance().persistir(usuarioExistente);
        request.session().attribute(
            "mensajeExito",
            "El usuario ha sido editado con éxito.");
      });
      response.redirect("/usuarios");
    } catch (Exception e) {
      usuarioExistente.vaciarContrasenia();
      request.session().attribute("mensajeError", e.getMessage());
      request.session().attribute("usuario", usuarioExistente);
      response.redirect("/usuarios/" + usuarioExistente.getId());
    }

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

      request.session().attribute(
          "mensajeExito",
          "El usuario ha sido eliminado con éxito.");
    });

    response.redirect("/usuarios");
    return null;
  }

  private Usuario getUsuarioDeRequest(Request request) {
    var usuario = request.queryParams("id") == null
        ? new Usuario()
        : RepositorioUsuarios.getInstance().porId(Integer.parseInt(request.queryParams("id")));

    usuario.setUsuario(request.queryParams("usuario"));
    usuario.setNombre(request.queryParams("nombre"));
    usuario.setApellido(request.queryParams("apellido"));
    usuario.setCorreoElectronico(request.queryParams("correo_electronico"));
    usuario.setAdmin(request.queryParams("es_admin") != null);

    if (!request.queryParams("contrasenia").isBlank()) {
      usuario.setContrasenia(request.queryParams("contrasenia"));
    }

    return usuario;
  }

  private void validarUsuario(Usuario usuario) throws Exception {
    if (usuario.getUsuario().isBlank() || usuario.getCorreoElectronico().isBlank() ||
        usuario.getNombre().isBlank() || usuario.getApellido().isBlank()) {
      throw new Exception("Uno o más campos no fueron completados.");
    }

    if (RepositorioUsuarios.getInstance().existeCorreoElectronico(usuario)) {
      throw new Exception("El correo electrónico ingresado ya se encuentra registrado.");
    }

    if (RepositorioUsuarios.getInstance().existeUsername(usuario)) {
      throw new Exception("El username ingresado ya se encuentra registrado.");
    }
  }

  private void validarContrasenia(String contrasenia) throws ValidacionContrasenaException {
    var validador = new ValidadorContrasena(Arrays.asList(
        new PoliticaLongitud(8, 16),
        new PoliticaRegex(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
            "La contraseña debe tener al menos una letra minúscula, una mayúscula," +
                " un número y un caracter especial.")
    ));

    validador.validar(contrasenia);
  }
}
