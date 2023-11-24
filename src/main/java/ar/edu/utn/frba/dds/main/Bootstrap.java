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

    usuario.setAdmin(true);

    var mati = new Usuario(
        "mati",
        "12345",
        "Mati",
        "Rodr",
        "mrodriguezstina@frba.utn.edu.ar",
        RepositorioComunidades.getInstance(),
        RepositorioNotificaciones.getInstance()
    );
    mati.setAdmin(true);

    RepositorioUsuarios.getInstance().persistir(usuario);
    RepositorioUsuarios.getInstance().persistir(mati);
    var entidad = new Entidad("Subte A", TipoDeEntidad.SUBTERRANEO);

    var medrano = new Ubicacion(-34.6033341, -58.4206027);

    var establecimiento =
        new Establecimiento("Estación Carabobo", entidad, medrano);

    var establecimiento2 =
        new Establecimiento("Estación Puan", entidad, medrano);
    var establecimiento3 =
        new Establecimiento("Estación Primera Junta", entidad, medrano);
    var establecimiento4 =
        new Establecimiento("Estación Acoyte", entidad, medrano);

    Servicio ascensor = new Servicio(
        "Ascensor - acceso a estación",
        TipoDeServicio.ASCENSORES
    );
    Servicio ascensor2 = new Servicio(
        "Ascensor - acceso a estación",
        TipoDeServicio.ASCENSORES
    );
    Servicio escalera = new Servicio(
        "Escalera mecanica - ascenso a estación",
        TipoDeServicio.ESCALERAS_MECANICAS
    );
    Servicio escalera2 = new Servicio(
        "Escalera mecanica - descenso a andén",
        TipoDeServicio.ESCALERAS_MECANICAS
    );

    establecimiento2.agregarServicio(escalera);
    establecimiento3.agregarServicio(escalera2);
    establecimiento4.agregarServicio(ascensor2);
    establecimiento.agregarServicio(ascensor);

    entidad.agregarEstablecimiento(establecimiento2);
    entidad.agregarEstablecimiento(establecimiento3);
    entidad.agregarEstablecimiento(establecimiento4);
    entidad.agregarEstablecimiento(establecimiento);


    RepositorioEntidades.getInstance().persistir(entidad);

    var nosMovemosEnSubte = new Comunidad("Nos movemos en subte", new ServicioGoogleMaps());
    var incidente = new Incidente(ascensor, "Se detuvo el ascensor en el subsuelo",
        LocalDateTime.now(), usuario);

    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor2);
    nosMovemosEnSubte.agregarServicioDeInteres(escalera);
    nosMovemosEnSubte.agregarServicioDeInteres(escalera2);
    nosMovemosEnSubte.agregarMiembro(usuario);
    nosMovemosEnSubte.agregarMiembro(mati);
    nosMovemosEnSubte.agregarIncidente(incidente);

    RepositorioComunidades.getInstance().persistir(nosMovemosEnSubte);

    var vecinos = new Comunidad("Vecinos de Plaza Houssay", new ServicioGoogleMaps());
    vecinos.agregarMiembro(usuario);
    RepositorioComunidades.getInstance().persistir(vecinos);

    var tren = new Comunidad("Usuarios de Tren Mitre", new ServicioGoogleMaps());
    tren.agregarMiembro(usuario);
    tren.agregarServicioDeInteres(escalera2);
    RepositorioComunidades.getInstance().persistir(tren);

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
