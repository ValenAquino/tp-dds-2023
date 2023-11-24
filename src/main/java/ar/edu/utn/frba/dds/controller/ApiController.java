package ar.edu.utn.frba.dds.controller;

import ar.edu.utn.frba.dds.controller.response.ApiResponse;
import ar.edu.utn.frba.dds.model.entidades.Comunidad;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioServicios;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import ar.edu.utn.frba.dds.model.ubicacion.implementaciones.ServicioGoogleMaps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;

public class ApiController implements WithSimplePersistenceUnit {
  public Response listarComunidades(Request request, Response response) {
    var gson = new Gson();

    try {
      var comunidades = RepositorioComunidades.getInstance().todas();
      response.status(200);
      response.body(gson.toJson(new ApiResponse(true, "Comunidades", formatearComunidades(comunidades))));
    } catch (RuntimeException e) {
      response.status(500);
      response.body(gson.toJson(new ApiResponse(false, "Ocurrió un error al obtener las comunidades", null)));
    }

    return response;
  }

  public Response detalleComunidad(Request request, Response response) {
    var gson = new Gson();
    var comunidadId = Integer.parseInt(request.params("id"));

    try {
      var comunidad = RepositorioComunidades.getInstance().porId(comunidadId);
      response.status(200);
      response.body(gson.toJson(new ApiResponse(true, "Detalle de comunidad", formatearComunidades(List.of(comunidad)))));
    } catch (RuntimeException e) {
      response.status(500);
      response.body(gson.toJson(new ApiResponse(false, "Ocurrió un error al obtener el detalle de la comunidad", null)));
    }

    return response;
  }

  //{"nombre": "Hinchas de Racing", "usuarios": [{"id": 1}], "servicios": [{"id": 2}]}
  public Response crearComunidad(Request request, Response response) {
    var gson = new Gson();

    try {
      withTransaction(() -> {
        var comunidad = deserializarComunidad(request, gson);
        RepositorioComunidades.getInstance().persistir(comunidad);
        response.status(200);
        response.body(gson.toJson(new ApiResponse(true, "Comunidad creada con éxito", formatearComunidades(List.of(comunidad)))));
      });
    } catch (RuntimeException e) {
      response.status(500);
      response.body(gson.toJson(new ApiResponse(false, "Ocurrió un error al intentar crear la comunidad", null)));
    }

    return response;
  }

  //{"nombre": "Hinchas de Racing", "usuarios": [{"id": 1}], "servicios": [{"id": 2}]}
  public Response editarComunidad(Request request, Response response) {
    var gson = new Gson();

    try {
      withTransaction(() -> {
        var comunidad = deserializarComunidad(request, gson);
        RepositorioComunidades.getInstance().persistir(comunidad);
        response.status(200);
        response.body(gson.toJson(new ApiResponse(true, "Comunidad editada con éxito", formatearComunidades(List.of(comunidad)))));
      });
    } catch (RuntimeException e) {
      response.status(500);
      response.body(gson.toJson(new ApiResponse(false, "Ocurrió un error al intentar editar la comunidad", null)));
    }

    return response;
  }

  public Response eliminarComunidad(Request request, Response response) {
    Gson gson = new Gson();

    try {
      withTransaction(() -> {
        var comunidadId = Integer.parseInt(request.params("id"));
        var comunidad = RepositorioComunidades.getInstance().porId(comunidadId);
        RepositorioComunidades.getInstance().eliminar(comunidad);
      });
      response.status(200);
      response.body(gson.toJson(new ApiResponse(true, "Comunidad eliminada con éxito", null)));
    } catch (RuntimeException e) {
      response.status(500);
      response.body(gson.toJson(new ApiResponse(false, "Ocurrió un error al intentar eliminar la comunidad", null)));
    }

    return response;
  }

  private List<Map<String, Object>> formatearComunidades(List<Comunidad> comunidades) {
    List<Map<String, Object>> results = new ArrayList<>();

    for (Comunidad comunidad : comunidades) {
      Map<String, Object> comunidadMap = new HashMap<>();
      comunidadMap.put("id", comunidad.getId());
      comunidadMap.put("nombre", comunidad.getNombre());

      var usuarios = new ArrayList<>();

      for (Usuario miembro : comunidad.getMiembros()) {
        var usuario = new HashMap<>();
        usuario.put("id", miembro.getId());
        usuario.put("nombre", miembro.getNombre());
        usuario.put("apellido", miembro.getApellido());
        usuario.put("username", miembro.getUsuario());

        usuarios.add(usuario);
      }

      comunidadMap.put("miembros", usuarios);

      var servicios = new ArrayList<>();

      for (Servicio servicioDeInteres : comunidad.getServiciosDeInteres()) {
        var servicio = new HashMap<>();
        servicio.put("id", servicioDeInteres.getId());
        servicio.put("descripcion", servicioDeInteres.getDescripcion());
        servicio.put("establecimiento", servicioDeInteres.getEstablecimiento().getNombre());

        servicios.add(servicio);
      }

      comunidadMap.put("servicios", servicios);

      results.add(comunidadMap);
    }

    return results;
  }

  private Comunidad deserializarComunidad(Request request, Gson gson) {
    Comunidad comunidad = null;
    var jsonObject = gson.fromJson(request.body(), JsonObject.class);

    var nombre = jsonObject.get("nombre").getAsString();

    if (request.params("id") != null) {
      int comunidadId = Integer.parseInt(request.params("id"));
      comunidad = RepositorioComunidades.getInstance().porId(comunidadId);
    } else {
      comunidad = new Comunidad(nombre, new ServicioGoogleMaps());
    }

    var usersArray = jsonObject.get("usuarios").getAsJsonArray();
    var servicesArray = jsonObject.get("servicios").getAsJsonArray();

    comunidad.eliminarMiembros();

    for (JsonElement user : usersArray) {
      var usuarioObject = user.getAsJsonObject();
      var usuario = RepositorioUsuarios.getInstance().porId(usuarioObject.get("id").getAsInt());
      comunidad.agregarMiembro(usuario);
    }

    comunidad.eliminarServiciosDeInteres();

    for (JsonElement service : servicesArray) {
      var servicioObject = service.getAsJsonObject();
      var servicio = RepositorioServicios.getInstance().porId(servicioObject.get("id").getAsInt());
      comunidad.agregarServicioDeInteres(servicio);
    }

    return comunidad;
  }
}
