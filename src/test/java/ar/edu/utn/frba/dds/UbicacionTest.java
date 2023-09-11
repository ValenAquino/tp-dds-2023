package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Comunidad;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Ubicacion;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.ubicacion.ServicioMapas;
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
  private final Usuario cornelioSaavedra = new Usuario(
      "cornelio.saavedra",
      "",
      "Cornelio",
      "Saavedra",
      "saavedra@primerajunta.gob"
  );
  private ServicioMapas servicioMapas;
  private MedioDeComunicacion medioDeComunicacion;
  private Servicio ascensor;
  private Servicio escaleraMecanica;
  private Servicio banioDeHombres;
  private Comunidad nosLiberamos;

  private RepositorioComunidades repositorioComunidades;

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

    when(servicioMapas.ubicacionActual(cornelioSaavedra.getCorreoElectronico()))
        .thenReturn(plazaDeMayo); // Set Ubicacion del Usuario

    medioDeComunicacion = mock(MedioDeComunicacion.class);
    nosLiberamos = new Comunidad(servicioMapas);
    nosLiberamos.agregarMiembro(cornelioSaavedra);
    cornelioSaavedra.setMedioDeComunicacion(medioDeComunicacion);

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
    when(repositorioComunidades.getComunidadesDe(cornelioSaavedra)).thenReturn(
        Collections.singletonList(
            nosLiberamos
        )
    );  // Set comunidades del usuario
    nosLiberamos.reportarIncidente(ascensor, "Fuera de servicio", LocalDateTime.now(), cornelioSaavedra);
    incidenteACerrar = nosLiberamos.reportarIncidente(escaleraMecanica, "Fuera de servicio", LocalDateTime.now(), cornelioSaavedra);
    nosLiberamos.reportarIncidente(banioDeHombres, "Fuera de servicio", LocalDateTime.now(), cornelioSaavedra);
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

    incidenteACerrar.cerrar();

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