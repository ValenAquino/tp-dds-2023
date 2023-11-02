package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
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

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("comunidades", Integer.valueOf(request.params("id")));
    return new ModelAndView(modelo, "incidentes/incidentes.html.hbs");
  }
}
