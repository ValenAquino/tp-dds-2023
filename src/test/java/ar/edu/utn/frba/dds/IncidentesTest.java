package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.*;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.notificaciones.*;
import ar.edu.utn.frba.dds.notificaciones.horarios.CalendarioNotificaciones;
import ar.edu.utn.frba.dds.notificaciones.horarios.RangoHorario;
import ar.edu.utn.frba.dds.notificaciones.medios.*;
import ar.edu.utn.frba.dds.ubicacion.ServicioMapas;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class IncidentesTest {

  private Usuario usuarioQueUsaSubte;
  private Usuario reportante;
  private MedioDeComunicacion medioDeComunicacion;
  private WhatsAppSender whatsAppSender;
  private MailSender mailSender;
  private final Servicio ascensor = new Servicio(
      "Ascensor - acceso a estación",
      TipoDeServicio.ASCENSORES
  );
  private final Servicio escaleraMecanica = new Servicio(
      "Escalera mecánica - acceso a andén",
      TipoDeServicio.ESCALERAS_MECANICAS
  );
  private Comunidad nosMovemosEnSubte;

  private ServicioMapas servicioMapas;

  @BeforeEach
  public void inicializar() {
    usuarioQueUsaSubte = new Usuario(
        "subte.master",
        "",
        "Subte",
        "Master",
        "subtemaster@gmail.com"
    );

    reportante = new Usuario(
        "subte.reportante",
        "",
        "Subte",
        "Reportante",
        "subtereportante@gmail.com"
    );

    medioDeComunicacion = mock(MedioDeComunicacion.class);
    whatsAppSender = mock(WhatsAppSender.class);
    mailSender = mock(MailSender.class);
    nosMovemosEnSubte = new Comunidad(servicioMapas);

    usuarioQueUsaSubte.setMedioDeComunicacion(medioDeComunicacion);

    nosMovemosEnSubte = new Comunidad(servicioMapas);
    nosMovemosEnSubte.agregarMiembro(usuarioQueUsaSubte);
    nosMovemosEnSubte.agregarMiembro(reportante);

    RepositorioComunidades.getInstance().agregarComunidad(nosMovemosEnSubte);
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

    reportante.reportarIncidente(escaleraMecanica, "Fuera de servicio");

    verify(medioDeComunicacion, never()).notificarReporteDeIncidente(any(), eq(reportante));
  }

  @Test
  public void seLlamaElMetodoDeMailCuandoElUsuarioEligeMail() {
    nosMovemosEnSubte.agregarServicioDeInteres(escaleraMecanica);
    usuarioQueUsaSubte.setMedioDeComunicacion(mailSender);

    reportante.reportarIncidente(escaleraMecanica, "Fuera de servicio");

    verify(mailSender).notificarReporteDeIncidente(any(), eq(usuarioQueUsaSubte));
  }
  @Test
  public void seLlamaElMetodoDeWhatsappCuandoElUsuarioEligeWhatsapp() {
    nosMovemosEnSubte.agregarServicioDeInteres(escaleraMecanica);
    usuarioQueUsaSubte.setMedioDeComunicacion(whatsAppSender);

    reportante.reportarIncidente(escaleraMecanica, "Fuera de servicio");

    verify(whatsAppSender).notificarReporteDeIncidente(any(), eq(usuarioQueUsaSubte));
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

    verify(medioDeComunicacion).notificarReporteDeIncidente(any(), eq(usuarioQueUsaSubte));
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

    verify(medioDeComunicacion, never()).notificarReporteDeIncidente(any(), eq(usuarioQueUsaSubte));
  }

  @Test
  public void seLlamaElMetodoNotificarCuandoNoSeConfiguraHorario() {
    usuarioQueUsaSubte.setCalendarioNotificaciones(null);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);

    reportante.reportarIncidente(ascensor, "Fuera de servicio");

    verify(medioDeComunicacion).notificarReporteDeIncidente(any(), eq(usuarioQueUsaSubte));
  }
}
