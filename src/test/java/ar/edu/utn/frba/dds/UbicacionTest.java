package ar.edu.utn.frba.dds;

import static org.mockito.Mockito.*;

import ar.edu.utn.frba.dds.entidades.Comunidad;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.entidades.Ubicacion;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.ubicacion.ServicioMapas;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UbicacionTest {

  private ServicioMapas servicioMapas;
  private MedioDeComunicacion medioDeComunicacion;
  private final Ubicacion plazaDeMayo = new Ubicacion(-34.608421330, -58.372169490);
  private final Usuario cornelioSaavedra = new Usuario(
      "cornelio.saavedra",
      "",
      "Cornelio",
      "Saavedra",
      "saavedra@primerajunta.gob"
  );
  private  Servicio ascensor;
  private Servicio escaleraMecanica;
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


    nosLiberamos.agregarServicioDeInteres(ascensor);
    nosLiberamos.agregarServicioDeInteres(escaleraMecanica);

    repositorioComunidades = mock(RepositorioComunidades.class);
    when(repositorioComunidades.getComunidadesDe(cornelioSaavedra)).thenReturn(
        Collections.singletonList(
            nosLiberamos
        )
    );  // Set comunidades del usuario
    nosLiberamos.abrirIncidente(ascensor, "Fuera de servicio");
    incidenteACerrar = nosLiberamos.abrirIncidente(escaleraMecanica, "aaaaaaa");
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
        .sugerirRevisionDeIncidente(any(), eq(cornelioSaavedra));
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
        .sugerirRevisionDeIncidente(any(), eq(cornelioSaavedra));
  }
}