package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Comunidad;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioNotificaciones;
import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.notificaciones.NotificacionNuevoIncidente;
import ar.edu.utn.frba.dds.notificaciones.horarios.CalendarioNotificaciones;
import ar.edu.utn.frba.dds.notificaciones.horarios.RangoHorario;
import ar.edu.utn.frba.dds.notificaciones.medios.MailSender;
import ar.edu.utn.frba.dds.notificaciones.medios.WhatsAppSender;
import ar.edu.utn.frba.dds.ubicacion.ServicioMapas;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class IncidentesTest {

  private final Servicio ascensor = new Servicio(
      "Ascensor - acceso a estación",
      TipoDeServicio.ASCENSORES
  );
  private final Servicio escaleraMecanica = new Servicio(
      "Escalera mecánica - acceso a andén",
      TipoDeServicio.ESCALERAS_MECANICAS
  );
  private Usuario usuarioQueUsaSubte;
  private Usuario reportante;
  private MedioDeComunicacion medioDeComunicacion;
  private WhatsAppSender whatsAppSender;
  private MailSender mailSender;
  private Comunidad nosMovemosEnSubte;
  private RepositorioComunidades repositorioComunidades;
  private RepositorioNotificaciones repositorioNotificaciones;

  private ServicioMapas servicioMapas;

  @BeforeEach
  public void inicializar() {
    repositorioComunidades = mock(RepositorioComunidades.class);
    repositorioNotificaciones = mock(RepositorioNotificaciones.class);

    usuarioQueUsaSubte = new Usuario(
        "subte.master",
        "",
        "Subte",
        "Master",
        "subtemaster@gmail.com",
        repositorioComunidades,
        repositorioNotificaciones
    );

    reportante = new Usuario(
        "subte.reportante",
        "",
        "Subte",
        "Reportante",
        "subtereportante@gmail.com",
        repositorioComunidades,
        repositorioNotificaciones
    );

    medioDeComunicacion = mock(MedioDeComunicacion.class);
    whatsAppSender = mock(WhatsAppSender.class);
    mailSender = mock(MailSender.class);
    nosMovemosEnSubte = new Comunidad(servicioMapas);

    usuarioQueUsaSubte.setMedioDeComunicacion(medioDeComunicacion);

    nosMovemosEnSubte = new Comunidad(servicioMapas);
    nosMovemosEnSubte.agregarMiembro(usuarioQueUsaSubte);
    nosMovemosEnSubte.agregarMiembro(reportante);
  }

  @Test
  public void unUsuarioPuedeReportarUnIncidente() {
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    reportante.reportarIncidente(ascensor, "Fuera de servicio");

    assertEquals(1, nosMovemosEnSubte.getIncidentes().size());
  }

  @Test
  public void unUsuarioPuedeCerrarUnIncidenteEnUnaComunidad() {
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    reportante.reportarIncidente(ascensor, "Fuera de servicio");

    var incidente = nosMovemosEnSubte.getIncidentes().get(0);

    nosMovemosEnSubte.cerrarIncidente(incidente);

    assertTrue(incidente.estaResuelto());
    assertNotNull(incidente.getFechaResolucion());
  }

  @Test
  public void puedoConsultarIncidentesPorEstado() {
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.agregarServicioDeInteres(escaleraMecanica);

    reportante.reportarIncidente(ascensor, "Fuera de servicio");
    reportante.reportarIncidente(escaleraMecanica, "Fuera de servicio");

    var incidenteAscensor = nosMovemosEnSubte.getIncidentesAbiertos().get(0);
    var incidenteEscalera = nosMovemosEnSubte.getIncidentesAbiertos().get(1);

    nosMovemosEnSubte.cerrarIncidente(incidenteAscensor);

    assertEquals(1, nosMovemosEnSubte.getIncidentesAbiertos().size());
    assertEquals(1, nosMovemosEnSubte.getIncidentesResueltos().size());
    assertEquals(incidenteAscensor, nosMovemosEnSubte.getIncidentesResueltos().get(0));
    assertEquals(incidenteEscalera, nosMovemosEnSubte.getIncidentesAbiertos().get(0));
  }

  @Test
  public void elUsuarioReportanteNoEsNotificado() {
    nosMovemosEnSubte.agregarServicioDeInteres(escaleraMecanica);
    Usuario reportanteMock = mock(Usuario.class);
    reportanteMock.reportarIncidente(escaleraMecanica, "Fuera de servicio");

    verify(reportanteMock, never()).notificar(any());
  }

  @Test
  public void seLlamaElMetodoDeMailCuandoElUsuarioEligeMail() {
    nosMovemosEnSubte.agregarServicioDeInteres(escaleraMecanica);
    usuarioQueUsaSubte.setMedioDeComunicacion(mailSender);

    reportante.reportarIncidente(escaleraMecanica, "Fuera de servicio");

    verify(mailSender).procesarNotificacion(any(NotificacionNuevoIncidente.class));
  }

  @Test
  public void seLlamaElMetodoDeWhatsappCuandoElUsuarioEligeWhatsapp() {
    nosMovemosEnSubte.agregarServicioDeInteres(escaleraMecanica);
    usuarioQueUsaSubte.setMedioDeComunicacion(whatsAppSender);

    reportante.reportarIncidente(escaleraMecanica, "Fuera de servicio");

    verify(whatsAppSender).procesarNotificacion(any(NotificacionNuevoIncidente.class));
  }

  @Test
  public void seLlamaElMetodoNotificarCuandoEsHorarioConfigurado() {
    LocalDateTime fecha = LocalDateTime.now();
    DayOfWeek dayOfWeek = fecha.getDayOfWeek();
    LocalTime horario = fecha.toLocalTime();

    Map<DayOfWeek, RangoHorario> horarios = new HashMap<>();
    horarios.put(dayOfWeek, new RangoHorario(horario.minusHours(1), horario.plusHours(1)));
    var calendarioNotificaciones = new CalendarioNotificaciones(horarios);

    usuarioQueUsaSubte.setCalendarioNotificaciones(calendarioNotificaciones);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);

    reportante.reportarIncidente(ascensor, "Fuera de servicio");

    verify(medioDeComunicacion).procesarNotificacion(any());
  }

  @Test
  public void noSeLlamaElMetodoNotificarCuandoNoEsHorarioConfigurado() {
    LocalDateTime fecha = LocalDateTime.now();
    DayOfWeek dayOfWeek = fecha.getDayOfWeek();
    LocalTime horario = fecha.toLocalTime();

    Map<DayOfWeek, RangoHorario> horarios = new HashMap<>();
    horarios.put(dayOfWeek, new RangoHorario(horario.plusHours(1), horario.plusHours(2)));
    var calendarioNotificaciones = new CalendarioNotificaciones(horarios);

    usuarioQueUsaSubte.setCalendarioNotificaciones(calendarioNotificaciones);
    nosMovemosEnSubte.agregarMiembro(usuarioQueUsaSubte);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);

    reportante.reportarIncidente(ascensor, "Fuera de servicio");

    verify(medioDeComunicacion, never()).procesarNotificacion(any());
  }

  @Test
  public void seLlamaElMetodoNotificarCuandoNoSeConfiguraHorario() {
    usuarioQueUsaSubte.setCalendarioNotificaciones(null);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);

    reportante.reportarIncidente(ascensor, "Fuera de servicio");

    verify(medioDeComunicacion).procesarNotificacion(any());
  }
}
