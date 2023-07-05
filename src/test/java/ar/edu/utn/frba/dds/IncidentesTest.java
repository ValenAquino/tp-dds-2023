package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.*;
import ar.edu.utn.frba.dds.notificaciones.*;
import ar.edu.utn.frba.dds.notificaciones.horarios.CalendarioNotificaciones;
import ar.edu.utn.frba.dds.notificaciones.horarios.RangoHorario;
import ar.edu.utn.frba.dds.notificaciones.medios.*;
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

  @BeforeEach
  public void inicializar() {
    usuarioQueUsaSubte = new Usuario(
        "subte.master",
        "",
        "Subte",
        "Master",
        "subtemaster@gmail.com"
    );
    medioDeComunicacion = mock(MedioDeComunicacion.class);
    whatsAppSender = mock(WhatsAppSender.class);
    mailSender = mock(MailSender.class);
    nosMovemosEnSubte = new Comunidad();
  }

  @Test
  public void puedoAbrirUnIncidenteEnUnaComunidad() {
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio");
    assertEquals(1, nosMovemosEnSubte.getIncidentes().size());
  }

  @Test
  public void abrirIncidenteDeServicioNoInteresLanzaExcepcion() {
    assertThrows(RuntimeException.class, () ->
        nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio"));
  }

  @Test
  public void puedoCerrarUnIncidenteEnUnaComunidad() {
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    Incidente incidente = nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio");
    incidente.cerrar();
    assertTrue(incidente.estaResuelto());
    assertNotNull(incidente.getFechaResolucion());
  }

  @Test
  public void puedoConsultarIncidentesPorEstado() {
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.agregarServicioDeInteres(escaleraMecanica);

    Incidente incidenteAscensor = nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio");
    Incidente incidenteEscalera = nosMovemosEnSubte.abrirIncidente(escaleraMecanica, "Fuera de servicio");
    incidenteAscensor.cerrar();

    assertEquals(1, nosMovemosEnSubte.getIncidentesAbiertos().size());
    assertEquals(1, nosMovemosEnSubte.getIncidentesResueltos().size());
    assertEquals(incidenteAscensor, nosMovemosEnSubte.getIncidentesResueltos().get(0));
    assertEquals(incidenteEscalera, nosMovemosEnSubte.getIncidentesAbiertos().get(0));
  }

  @Test
  public void seLlamaElMetodoNotificarDeUnUsuarioInteresado() {
    nosMovemosEnSubte.agregarMiembro(usuarioQueUsaSubte);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    usuarioQueUsaSubte.agregarServicioDeInteres(ascensor);
    usuarioQueUsaSubte.setMedioDeComunicacion(medioDeComunicacion);
    nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio");
    verify(medioDeComunicacion).notificarAperturaDeIncidente(any());
  }

  @Test
  public void noSeLlamaElMetodoNotificarDeUnUsuarioNoInteresado() {
    nosMovemosEnSubte.agregarMiembro(usuarioQueUsaSubte);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    usuarioQueUsaSubte.setMedioDeComunicacion(medioDeComunicacion);
    nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio");
    verify(medioDeComunicacion, never()).notificarAperturaDeIncidente(any());
  }

  @Test
  public void seLlamaElMetodoDeMailCuandoElUsuarioEligeMail() {
    nosMovemosEnSubte.agregarMiembro(usuarioQueUsaSubte);
    nosMovemosEnSubte.agregarServicioDeInteres(escaleraMecanica);
    usuarioQueUsaSubte.setMedioDeComunicacion(mailSender);
    usuarioQueUsaSubte.agregarServicioDeInteres(escaleraMecanica);
    nosMovemosEnSubte.abrirIncidente(escaleraMecanica, "Fuera de servicio");
    verify(mailSender).notificarAperturaDeIncidente(any());
  }

  @Test
  public void seLlamaElMetodoDeWhatsappCuandoElUsuarioEligeWhatsapp() {
    nosMovemosEnSubte.agregarMiembro(usuarioQueUsaSubte);
    nosMovemosEnSubte.agregarServicioDeInteres(escaleraMecanica);
    usuarioQueUsaSubte.setMedioDeComunicacion(whatsAppSender);
    usuarioQueUsaSubte.agregarServicioDeInteres(escaleraMecanica);
    nosMovemosEnSubte.abrirIncidente(escaleraMecanica, "Fuera de servicio");
    verify(whatsAppSender).notificarAperturaDeIncidente(any());
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
    usuarioQueUsaSubte.setMedioDeComunicacion(medioDeComunicacion);
    usuarioQueUsaSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.agregarMiembro(usuarioQueUsaSubte);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio");

    verify(medioDeComunicacion).notificarAperturaDeIncidente(any());
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
    usuarioQueUsaSubte.setMedioDeComunicacion(medioDeComunicacion);
    usuarioQueUsaSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.agregarMiembro(usuarioQueUsaSubte);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio");

    verify(medioDeComunicacion, never()).notificarAperturaDeIncidente(any());
  }

  @Test
  public void seLlamaElMetodoNotificarCuandoNoSeConfiguraHorario() {
    usuarioQueUsaSubte.setCalendarioNotificaciones(null);
    usuarioQueUsaSubte.setMedioDeComunicacion(medioDeComunicacion);
    usuarioQueUsaSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.agregarMiembro(usuarioQueUsaSubte);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio");

    verify(medioDeComunicacion).notificarAperturaDeIncidente(any());
  }

}