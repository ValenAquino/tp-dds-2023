package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.CustomModel;
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

public class ComunidadesController implements WithSimplePersistenceUnit {
  public ModelAndView listar(Request request, Response response) {
    Usuario usuarioLogueado = SessionController.usuarioLogueado(request);
    var comunidades = RepositorioComunidades.getInstance().comunidadesPorUsuario(usuarioLogueado);
    Map<String, Object> modelo = new CustomModel("Comunidades", request);
    modelo.put("comunidades", comunidades);

    if(usuarioLogueado.esAdmin()){
      return new ModelAndView(modelo, "pages/comunidadesDashboard.html.hbs");
    }
    return new ModelAndView(modelo, "pages/comunidadesCards.html.hbs");
  }

  public Void eliminar(Request request, Response response) {
    withTransaction(() -> {
      var comunidadId = Integer.parseInt(request.queryParams("id"));
      var comunidad = RepositorioComunidades.getInstance().porId(comunidadId);
      RepositorioComunidades.getInstance().eliminar(comunidad);
    });

    response.redirect("/comunidades");
    return null;
  }
}
