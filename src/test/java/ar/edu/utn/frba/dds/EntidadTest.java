package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Establecimiento;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.notificaciones.Notificacion;
import ar.edu.utn.frba.dds.notificaciones.NotificacionNuevoIncidente;
import ar.edu.utn.frba.dds.notificaciones.horarios.CalendarioNotificaciones;
import ar.edu.utn.frba.dds.notificaciones.horarios.RangoHorario;
import ar.edu.utn.frba.dds.notificaciones.medios.MailSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class EntidadTest {
  private Entidad entidad;
  private Establecimiento establecimiento1;
  private Establecimiento establecimiento2;
  private Establecimiento establecimiento3;
  private Servicio servicio1;
  private Servicio servicio2;
  private Servicio servicio3;
  private Usuario usuarioQueUsaSubte;

  private MailSender mailSender;

  private CalendarioNotificaciones calendarioQuePermite;
  private CalendarioNotificaciones calendarioQueNoPermite;
  private RangoHorario rangoHorarioCompleto;
  private RangoHorario rangoHorarioVacio;
  private Map<DayOfWeek, RangoHorario> horarios;

  @BeforeEach
  public void setUp() {
    entidad = new Entidad("entidad", TipoDeEntidad.SUBTERRANEO);
    establecimiento1 = new Establecimiento();
    establecimiento2 = new Establecimiento();
    establecimiento3 = new Establecimiento();
    servicio1 = new Servicio("servicioInestable", TipoDeServicio.BANIOS);
    servicio2 = new Servicio("ascensorInestable", TipoDeServicio.ASCENSORES);
    servicio3 = new Servicio("escalerasInestables", TipoDeServicio.ESCALERAS_MECANICAS);

    establecimiento1.agregarServicio(servicio1);
    establecimiento2.agregarServicio(servicio2);
    establecimiento3.agregarServicio(servicio3);

    entidad.agregarEstablecimiento(establecimiento1);
    entidad.agregarEstablecimiento(establecimiento2);

    horarios = new HashMap<>();

    usuarioQueUsaSubte = new Usuario(
        "subte.master",
        "",
        "Subte",
        "Master",
        "subtemaster@gmail.com"
    );



    rangoHorarioCompleto = new RangoHorario(LocalTime.MIN,LocalTime.MAX);
    rangoHorarioVacio = new RangoHorario(LocalTime.MIN,LocalTime.MIN);

    horarios.put(DayOfWeek.THURSDAY,rangoHorarioVacio);

    calendarioQueNoPermite = new CalendarioNotificaciones(horarios);

    horarios.put(DayOfWeek.MONDAY,rangoHorarioCompleto);
    horarios.put(DayOfWeek.TUESDAY,rangoHorarioCompleto);
    horarios.put(DayOfWeek.WEDNESDAY,rangoHorarioCompleto);
    horarios.put(DayOfWeek.THURSDAY,rangoHorarioCompleto);
    horarios.put(DayOfWeek.FRIDAY,rangoHorarioCompleto);
    horarios.put(DayOfWeek.SATURDAY,rangoHorarioCompleto);
    horarios.put(DayOfWeek.SUNDAY,rangoHorarioCompleto);

    calendarioQuePermite = new CalendarioNotificaciones(horarios);
  }

  @Test
  public void seObtienenLosIncidentesDeLaUltimaSemana() {
    LocalDateTime fechaActual = LocalDateTime.now();
    LocalDateTime fechaLimite = fechaActual.minusDays(7);

    Incidente incidente1 = new Incidente(servicio1, "Incidente1");
//    incidente1.setFecha(fechaLimite.plusDays(1)); // 6 dias atras

    Incidente incidente2 = new Incidente(servicio2, "Incidente2");
//    incidente2.setFecha(fechaLimite.minusDays(1)); // 8 dias atras

    Incidente incidente3 = new Incidente(servicio1, "Incidente3");
//    incidente3.setFecha(fechaLimite.plusDays(3)); // 4 dias atras

    Incidente incidente4 = new Incidente(servicio2, "Incidente2");
//    incidente4.setFecha(fechaActual.plusDays(1)); // 1 dia despues

//    // usar repo
//    List<Incidente> incidentesUltimaSemana = new ArrayList<>();
//
//    Assertions.assertEquals(2, incidentesUltimaSemana.size());
//    Assertions.assertTrue(incidentesUltimaSemana.contains(incidente1));
//    Assertions.assertFalse(incidentesUltimaSemana.contains(incidente2));
//    Assertions.assertTrue(incidentesUltimaSemana.contains(incidente3));
//    Assertions.assertFalse(incidentesUltimaSemana.contains(incidente4));
  }

  @Test
  public void unaEntidadPuedeReportarUnIncidenteEnUnServicioSuyo() {
    entidad.reportarIncidente(servicio1, "No anda la cadena");
    entidad.reportarIncidente(servicio2, "No funciona botón de piso 3");
    assertEquals(2, entidad.getIncidentesAbiertos().size());
  }

  @Test
  public void unaEntidadNoPuedeReportarUnIncidenteEnUnServicioAjeno() {
    Assertions.assertThrows(RuntimeException.class, () ->
        entidad.reportarIncidente(servicio3, "No funciona la escalera mecanica"));
  }

  @Test
  public void unUsuarioEsNotificadoPorLaAperturaDeUnIncidenteQueLeInteresa() {

    mailSender = mock(MailSender.class);
    usuarioQueUsaSubte.setMedioDeComunicacion(mailSender);
    usuarioQueUsaSubte.setCalendarioNotificaciones(calendarioQuePermite);
    entidad.agregarUsuarioInteresado(usuarioQueUsaSubte);

    entidad.reportarIncidente(servicio1, "No anda la cadena");

    ArgumentCaptor<Notificacion> notificacionCaptor = ArgumentCaptor.forClass(Notificacion.class);
    // Verificar que se haya llamado al método notificar con cualquier argumento
    verify(mailSender, times(1)).procesarNotificacion(notificacionCaptor.capture());

    //Notificacion notificacionCapturada = argumentCaptor.getValue();
    //Incidente incidenteEnNotificacion = notificacionCapturada.getIncidente();
    //assertEquals("No anda la cadena", incidenteEnNotificacion.getObservaciones());

    //verify(mailSender).notificarReporteDeIncidente(any(), eq(usuarioQueUsaSubte));
  }

  @Test
  public void unUsuarioNoEsNotificadoPorLaAperturaDeUnIncidenteQueNoLeInteresa() {
    Usuario usuarioQueUsaSubteSpy = spy(usuarioQueUsaSubte);

    usuarioQueUsaSubteSpy.setMedioDeComunicacion(mailSender);
    entidad.reportarIncidente(servicio1, "No anda la cadena");
    verify(usuarioQueUsaSubteSpy, never()).notificar(any());
  }
}
