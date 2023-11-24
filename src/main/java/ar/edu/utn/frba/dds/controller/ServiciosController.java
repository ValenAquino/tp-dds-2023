package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.CustomModel;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioServicios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ServiciosController implements WithSimplePersistenceUnit {
  public ModelAndView listar(Request request, Response response) {
    Usuario usuarioLogueado = SessionController.usuarioLogueado(request);
    Boolean reporteExitoso = request.session().attribute("reporte_exitoso");
    var servicios = RepositorioServicios.getInstance().porUsuario(usuarioLogueado);

    Map<String, Object> modelo = new CustomModel("Servicios", request);
    modelo.put("servicios", servicios);

    if(reporteExitoso!=null){
      request.session().removeAttribute("reporte_exitoso");
      modelo.put("reporte_exitoso",reporteExitoso);
    }
    return new ModelAndView(modelo, "pages/servicios.html.hbs");
  }
}
