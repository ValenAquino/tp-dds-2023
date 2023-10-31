package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.entidades.Comunidad;
import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.Ubicacion;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioNotificaciones;
import ar.edu.utn.frba.dds.model.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.model.ubicacion.ServicioMapas;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UbicacionTest {

  private final Ubicacion plazaDeMayo = new Ubicacion(-34.608421330, -58.372169490);
  private final Ubicacion lima = new Ubicacion(-34.627777777778, -58.381305555556);
  private Usuario cornelioSaavedra;
  private ServicioMapas servicioMapas;
  private MedioDeComunicacion medioDeComunicacion;
  private Servicio ascensor;
  private Servicio escaleraMecanica;
  private Servicio banioDeHombres;
  private Comunidad nosLiberamos;

  private RepositorioComunidades repositorioComunidades;
  private RepositorioNotificaciones repositorioNotificaciones;

  private Incidente incidenteACerrar;

  @BeforeEach
  public void inicializar() {
    servicioMapas = mock(ServicioMapas.class);
    when(servicioMapas.estanCerca(
        any(Ubicacion.class), // Cualquier ubicación
        any(Ubicacion.class), // Cualquier ubicación
        any(Long.class)
    )).thenReturn(true);
    when(servicioMapas.estanCerca(
        eq(plazaDeMayo), // Cualquier ubicación
        eq(lima), // Cualquier ubicación
        any(Long.class)
    )).thenReturn(false);

    medioDeComunicacion = mock(MedioDeComunicacion.class);
    nosLiberamos = new Comunidad(servicioMapas);

    ascensor = mock(Servicio.class);
    when(ascensor.getUbicacion()).thenReturn(plazaDeMayo);  // Set Ubicacion del Servicio

    escaleraMecanica = mock(Servicio.class);
    when(escaleraMecanica.getUbicacion()).thenReturn(plazaDeMayo);  // Set Ubicacion del Servicio

    banioDeHombres = mock(Servicio.class);
    when(banioDeHombres.getUbicacion()).thenReturn(lima);  // Set Ubicacion del Servicio

    nosLiberamos.agregarServicioDeInteres(banioDeHombres);
    nosLiberamos.agregarServicioDeInteres(ascensor);
    nosLiberamos.agregarServicioDeInteres(escaleraMecanica);

    repositorioComunidades = mock(RepositorioComunidades.class);

    nosLiberamos.reportarIncidente(ascensor, "Fuera de servicio", LocalDateTime.now(), cornelioSaavedra);
    incidenteACerrar = nosLiberamos.reportarIncidente(escaleraMecanica, "Fuera de servicio", LocalDateTime.now(), cornelioSaavedra);
    nosLiberamos.reportarIncidente(banioDeHombres, "Fuera de servicio", LocalDateTime.now(), cornelioSaavedra);

    repositorioNotificaciones = mock(RepositorioNotificaciones.class);

    cornelioSaavedra = new Usuario(
        "cornelio.saavedra",
        "",
        "Cornelio",
        "Saavedra",
        "saavedra@primerajunta.gob",
        repositorioComunidades,
        repositorioNotificaciones
    );

    when(repositorioComunidades.getComunidadesDe(cornelioSaavedra)).thenReturn(
        Collections.singletonList(
            nosLiberamos
        )
    );  // Set comunidades del usuario

    when(servicioMapas.ubicacionActual(cornelioSaavedra.getCorreoElectronico()))
        .thenReturn(plazaDeMayo); // Set Ubicacion del Usuario

    nosLiberamos.agregarMiembro(cornelioSaavedra);
    cornelioSaavedra.setMedioDeComunicacion(medioDeComunicacion);
  }

  @Test
  public void unUsuarioPuedeRecibirUnaSugerenciaDeRevisionDeIncidenteSiEstaCerca() {

    List<Incidente> listaIncidentesCercanosAbiertos =
        repositorioComunidades
            .getComunidadesDe(cornelioSaavedra)
            .stream()
            .flatMap(c -> c.getIncidentesAbiertosCercanosA(cornelioSaavedra).stream()).toList();

    listaIncidentesCercanosAbiertos.forEach(cornelioSaavedra::sugerirRevisionDeIncidente);
    verify(medioDeComunicacion, times(2))
        .procesarNotificacion(any());
  }

  @Test
  public void unUsuarioNoRecibeSugerenciaDeIncidenteCerrado() {

    incidenteACerrar.cerrar(LocalDateTime.now());

    List<Incidente> listaIncidentesCercanosAbiertos =
        repositorioComunidades
            .getComunidadesDe(cornelioSaavedra)
            .stream()
            .flatMap(c -> c.getIncidentesAbiertosCercanosA(cornelioSaavedra).stream()).toList();

    listaIncidentesCercanosAbiertos.forEach(cornelioSaavedra::sugerirRevisionDeIncidente);
    verify(medioDeComunicacion, times(1))
        .procesarNotificacion(any());
  }

  @Test
  public void unUsuarioNoRecibeSugerenciaDeIncidenteLejano() {

    List<Incidente> listaIncidentesCercanosAbiertos =
        repositorioComunidades
            .getComunidadesDe(cornelioSaavedra)
            .stream()
            .flatMap(c -> c.getIncidentesAbiertosCercanosA(cornelioSaavedra).stream()).toList();

    listaIncidentesCercanosAbiertos.forEach(cornelioSaavedra::sugerirRevisionDeIncidente);
    verify(medioDeComunicacion, times(2))
        .procesarNotificacion(any());
  }
}