package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.model.entidades.Establecimiento;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioEntidades;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncidentesController implements WithSimplePersistenceUnit {
  public ModelAndView nuevo(Request request, Response response){
    Map<String, Object> model = new HashMap<>();
    List<Establecimiento> establecimientos = RepositorioEntidades.getInstance()
        .todas()
        .stream()
        .flatMap(entidad -> entidad.getEstablecimientos().stream())
        .collect(Collectors.toList());
    List<Servicio> servicios = establecimientos
        .stream()
        .flatMap(establecimiento -> establecimiento.getServicios().stream())
        .collect(Collectors.toList());
    model.put("establecimientos", establecimientos);
    model.put("servicios", servicios);
    return new ModelAndView(model, "incidentes/reportarIncidente.html.hbs");
  }
  public ModelAndView reportarIncidente(Request request, Response response) {
    return new ModelAndView(null, "incidentes/reportarIncidente.html.hbs");
  }
}
