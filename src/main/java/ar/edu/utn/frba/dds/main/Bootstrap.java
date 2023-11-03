package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.model.entidades.*;
import ar.edu.utn.frba.dds.model.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.model.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.model.entidades.repositorios.*;
import ar.edu.utn.frba.dds.model.ubicacion.implementaciones.ServicioGoogleMaps;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDateTime;

public class Bootstrap implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    new Bootstrap().run();
  }

  public void run() {
    withTransaction(() -> {
      crearUsuario();
      crearIncidentes();
      crearIncidentesCerrados();
    });
  }

  private void crearIncidentesCerrados() {
    var entidad = new Entidad("Subte B", TipoDeEntidad.SUBTERRANEO);
    var correo = new Ubicacion(-34.6603376, -58.421703312);
    var establecimiento = new Establecimiento("Estación Alem", entidad, correo);

    Servicio escaleraMecanica = new Servicio(
        "Escalera mecanica",
        TipoDeServicio.ESCALERAS_MECANICAS
    );

    escaleraMecanica.setEstablecimiento(establecimiento);
    establecimiento.agregarServicio(escaleraMecanica);
    entidad.agregarEstablecimiento(establecimiento);

    RepositorioEntidades.getInstance().persistir(entidad);

    for (var i = 0; i < 5; i++) {
      var incidente = new Incidente(
          escaleraMecanica,
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
          LocalDateTime.now().minusDays(5).plusHours(i)
      );

      incidente.cerrar(LocalDateTime.now().minusDays(5).plusHours(i).plusMinutes(15));

      RepositorioIncidentes.getInstance().persistir(incidente);
    }
  }

  private void crearUsuario() {
    var usuario = new Usuario(
        "alumnofrba",
        "12345",
        "Alumno",
        "Modelo",
        "alumno@frba.utn.edu.ar",
        RepositorioComunidades.getInstance(),
        RepositorioNotificaciones.getInstance()
    );

    RepositorioUsuarios.getInstance().persistir(usuario);
  }

  private void crearIncidentes() {
    var usuario = new Usuario(
        "juan",
        "12345",
        "Juan",
        "Larra",
        "juanlarra@frba.utn.edu.ar",
        RepositorioComunidades.getInstance(),
        RepositorioNotificaciones.getInstance()
    );

    RepositorioUsuarios.getInstance().persistir(usuario);

    var entidad = new Entidad("Subte A", TipoDeEntidad.SUBTERRANEO);

    var medrano = new Ubicacion(-34.6033341, -58.4206027);

    var establecimiento =
        new Establecimiento("Estación Carabobo", entidad, medrano);

    Servicio ascensor = new Servicio(
        "Ascensor - acceso a estación",
        TipoDeServicio.ASCENSORES
    );

    ascensor.setEstablecimiento(establecimiento);

    establecimiento.agregarServicio(ascensor);

    entidad.agregarEstablecimiento(establecimiento);

    RepositorioEntidades.getInstance().persistir(entidad);

    var nosMovemosEnSubte = new Comunidad("Nos movemos en subte", new ServicioGoogleMaps());
    var incidente = new Incidente(ascensor, "Se detuvo el ascensor en el subsuelo",
        LocalDateTime.now(), usuario);

    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.agregarMiembro(usuario);
    nosMovemosEnSubte.agregarIncidente(incidente);

    RepositorioComunidades.getInstance().persistir(nosMovemosEnSubte);

    var vecinos = new Comunidad("Vecinos de plaza", new ServicioGoogleMaps());
    vecinos.agregarMiembro(usuario);
    RepositorioComunidades.getInstance().persistir(vecinos);

    RepositorioIncidentes.getInstance().persistir(incidente);

    for (var i = 0; i < 3; i++) {
      var incidente2 = new Incidente(ascensor,
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent venenatis sit amet arcu vitae hendrerit. In elementum neque in sapien viverra, vel auctor nibh dapibus.",
          LocalDateTime.now(), usuario);

      usuario.sugerirRevisionDeIncidente(incidente2);

      nosMovemosEnSubte.agregarIncidente(incidente2);
      RepositorioIncidentes.getInstance().persistir(incidente2);
    }
  }
}
