package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class HomeController implements WithSimplePersistenceUnit {
  public ModelAndView render(Request request, Response response) {
    Integer idUsuario = null;

    try {
      Object userIdObject = request.session().attribute("user_id");

      if (!(userIdObject instanceof Integer)) {
        throw new IllegalStateException("Sesión inválida");
      }

      idUsuario = (Integer) userIdObject;
    } catch (Exception e) {
      response.redirect("/login");
      return null;
    }

    Usuario usuarioLogueado = RepositorioUsuarios.getInstance().porId(idUsuario);

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("comunidades", RepositorioComunidades.getInstance().comunidadesDeUsuario(usuarioLogueado));
    modelo.put("incidentes", RepositorioIncidentes.getInstance().incidentesARevisarPara(usuarioLogueado));

    return new ModelAndView(modelo, "index.html.hbs");
  }
}
