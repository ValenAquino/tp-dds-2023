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
import ar.edu.utn.frba.dds.ubicacion.ServicioUbicacion;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UbicacionTest {

  private ServicioUbicacion servicioUbicacion;
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
  private final Servicio ascensor = new Servicio(
      "Ascensor - acceso a estación",
      TipoDeServicio.ASCENSORES
  );
  private final Servicio escaleraMecanica = new Servicio(
      "Escalera mecánica - acceso a andén",
      TipoDeServicio.ESCALERAS_MECANICAS
  );
  private Comunidad nosLiberamos;

  @BeforeEach
  public void inicializar() {
    servicioUbicacion = mock(ServicioUbicacion.class);
    servicioMapas = mock(ServicioMapas.class);
    medioDeComunicacion = mock(MedioDeComunicacion.class);
    nosLiberamos = new Comunidad();
    nosLiberamos.agregarMiembro(cornelioSaavedra);
    cornelioSaavedra.setMedioDeComunicacion(medioDeComunicacion);
    RepositorioComunidades.getInstance().agregarComunidad(nosLiberamos);
  }

  @Test
  public void unUsuarioPuedeRecibirUnaSugerenciaDeRevisionDeIncidenteSiEstaCerca() {
    var ascensorMock = mock(ascensor.getClass());
    when(ascensorMock.getUbicacion()).thenReturn(plazaDeMayo);
    when(servicioUbicacion.ubicacionActual(cornelioSaavedra.getCorreoElectronico()))
        .thenReturn(plazaDeMayo);

    nosLiberamos.agregarServicioDeInteres(ascensorMock);
    nosLiberamos.agregarServicioDeInteres(escaleraMecanica);

    cornelioSaavedra.reportarIncidente(ascensorMock, "Fuera de servicio");
    cornelioSaavedra.reportarIncidente(escaleraMecanica, "Fuera de servicio");

    var incidenteAbierto = nosLiberamos.getIncidentesAbiertos().get(0);
    var incidenteACerrar = nosLiberamos.getIncidentesAbiertos().get(1);

    incidenteACerrar.cerrar();

    when(servicioMapas.estanCerca(
        cornelioSaavedra.getUbicacionActual(servicioUbicacion),
        incidenteAbierto.getUbicacion(),
        200
    )).thenReturn(true);

    RepositorioComunidades repositorioComunidades = mock(RepositorioComunidades.class);
    when(repositorioComunidades.getComunidadesDe(cornelioSaavedra)).thenReturn(
        Collections.singletonList(
            nosLiberamos
        )
    );
    List<Incidente> listaIncidentesCercanosAbiertos =
        repositorioComunidades
        .getComunidadesDe(cornelioSaavedra)
        .stream()
        .flatMap(c -> c.getIncidentesAbiertos().stream())
        .filter(i ->
            servicioMapas.estanCerca(
                cornelioSaavedra.getUbicacionActual(servicioUbicacion),
                i.getUbicacion(),
                200
            )
        )
        .toList();
    // TODO: aumentar declaratividad, por ejemplo: getIncidentesAbiertosCercanosAUnUsuario

    listaIncidentesCercanosAbiertos.forEach(cornelioSaavedra::sugerirRevisionDeIncidente);
    verify(medioDeComunicacion).sugerirRevisionDeIncidente(any(), eq(cornelioSaavedra));
  }
}