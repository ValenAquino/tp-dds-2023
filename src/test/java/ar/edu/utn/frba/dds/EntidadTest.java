package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Establecimiento;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Ubicacion;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioNotificaciones;
import ar.edu.utn.frba.dds.notificaciones.Notificacion;
import ar.edu.utn.frba.dds.notificaciones.horarios.CalendarioNotificaciones;
import ar.edu.utn.frba.dds.notificaciones.horarios.RangoHorario;
import ar.edu.utn.frba.dds.notificaciones.medios.MailSender;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
  private Map<DayOfWeek, RangoHorario> horariosVacios;

  private RepositorioComunidades repositorioComunidades;
  private RepositorioNotificaciones repositorioNotificaciones;
  private RepositorioIncidentes repositorioIncidentes;


  @BeforeEach
  public void setUp() {

    Ubicacion facultadDeMedicina = new Ubicacion(-34.59977778, -58.39766667);
    Ubicacion castroBarros = new Ubicacion(-34.611806, -58.42175);
    Ubicacion medrano = new Ubicacion(-34.6033341,-58.4206027);

    entidad = new Entidad("entidad", TipoDeEntidad.SUBTERRANEO);
    establecimiento1 = new Establecimiento(entidad, facultadDeMedicina);
    establecimiento2 = new Establecimiento(entidad, castroBarros);
    establecimiento3 = new Establecimiento(entidad, medrano);
    servicio1 = new Servicio("servicioInestable", TipoDeServicio.BANIOS);
    servicio2 = new Servicio("ascensorInestable", TipoDeServicio.ASCENSORES);
    servicio3 = new Servicio("escalerasInestables", TipoDeServicio.ESCALERAS_MECANICAS);

    establecimiento1.agregarServicio(servicio1);
    establecimiento2.agregarServicio(servicio2);
    establecimiento3.agregarServicio(servicio3);

    entidad.agregarEstablecimiento(establecimiento1);
    entidad.agregarEstablecimiento(establecimiento2);

    horarios = new HashMap<>();
    horariosVacios = new HashMap<>();

    repositorioNotificaciones = mock(RepositorioNotificaciones.class);
    repositorioComunidades = mock(RepositorioComunidades.class);
    repositorioIncidentes = mock(RepositorioIncidentes.class);

    usuarioQueUsaSubte = new Usuario(
        "subte.master",
        "",
        "Subte",
        "Master",
        "subtemaster@gmail.com",
        repositorioComunidades,
        repositorioNotificaciones
    );

    rangoHorarioCompleto = new RangoHorario(LocalTime.MIN, LocalTime.MAX);
    rangoHorarioVacio = new RangoHorario(LocalTime.MIN, LocalTime.MIN);

    horariosVacios.put(DayOfWeek.THURSDAY, rangoHorarioVacio);

    calendarioQueNoPermite = new CalendarioNotificaciones(horariosVacios);

    horarios.put(DayOfWeek.MONDAY, rangoHorarioCompleto);
    horarios.put(DayOfWeek.TUESDAY, rangoHorarioCompleto);
    horarios.put(DayOfWeek.WEDNESDAY, rangoHorarioCompleto);
    horarios.put(DayOfWeek.THURSDAY, rangoHorarioCompleto);
    horarios.put(DayOfWeek.FRIDAY, rangoHorarioCompleto);
    horarios.put(DayOfWeek.SATURDAY, rangoHorarioCompleto);
    horarios.put(DayOfWeek.SUNDAY, rangoHorarioCompleto);

    calendarioQuePermite = new CalendarioNotificaciones(horarios);
  }

  @Test
  public void seObtienenLosIncidentesDeLaUltimaSemana() {
    LocalDateTime fechaActual = LocalDateTime.now();
    LocalDateTime fechaLimite = fechaActual.minusDays(7);

    Incidente incidente1 = new Incidente(servicio1,
        "Incidente1", fechaLimite.plusDays(1));

    Incidente incidente2 = new Incidente(servicio2,
        "Incidente2", fechaLimite.minusDays(1));

    Incidente incidente3 = new Incidente(servicio1,
        "Incidente3", fechaLimite.plusDays(3));

    Incidente incidente4 = new Incidente(servicio2,
        "Incidente2", fechaActual.plusDays(1));

    List<Incidente> incidentesUltimaSemana = List.of(incidente1, incidente3);

    when(repositorioIncidentes.ultimaSemana()).thenReturn(incidentesUltimaSemana);

    Assertions.assertEquals(2, repositorioIncidentes.ultimaSemana().size());
    Assertions.assertTrue(repositorioIncidentes.ultimaSemana().contains(incidente1));
    Assertions.assertFalse(repositorioIncidentes.ultimaSemana().contains(incidente2));
    Assertions.assertTrue(repositorioIncidentes.ultimaSemana().contains(incidente3));
    Assertions.assertFalse(repositorioIncidentes.ultimaSemana().contains(incidente4));
  }

  @Test
  public void unaEntidadPuedeReportarUnIncidenteEnUnServicioSuyo() {
    entidad.reportarIncidente(servicio1, LocalDateTime.now(), "No anda la cadena");
    entidad.reportarIncidente(servicio2, LocalDateTime.now(), "No funciona botón de piso 3");
    assertEquals(2, entidad.getIncidentesAbiertos().size());
  }

  @Test
  public void unaEntidadNoPuedeReportarUnIncidenteEnUnServicioAjeno() {
    Assertions.assertThrows(RuntimeException.class, () ->
        entidad.reportarIncidente(servicio3, LocalDateTime.now(), "No funciona la escalera mecanica"));
  }

  @Test
  public void unUsuarioEsNotificadoPorLaAperturaDeUnIncidenteQueLeInteresa() {
    mailSender = mock(MailSender.class);
    usuarioQueUsaSubte.setMedioDeComunicacion(mailSender);
    usuarioQueUsaSubte.setCalendarioNotificaciones(calendarioQuePermite);
    entidad.agregarUsuarioInteresado(usuarioQueUsaSubte);

    entidad.reportarIncidente(servicio1, LocalDateTime.now(), "No anda la cadena");

    ArgumentCaptor<Notificacion> notificacionCaptor = ArgumentCaptor.forClass(Notificacion.class);

    // Verificar que el medio de comunicacion llega a procesar la notificacion y que el receptor es el correcto
    verify(mailSender, times(1)).procesarNotificacion(notificacionCaptor.capture());
    assertEquals(usuarioQueUsaSubte, notificacionCaptor.getValue().getReceptor());

  }

  @Test
  public void unUsuarioNoEsNotificadoPorLaAperturaDeUnIncidenteQueNoLeInteresa() {
    Usuario usuarioQueUsaSubteSpy = spy(usuarioQueUsaSubte);
    usuarioQueUsaSubteSpy.setCalendarioNotificaciones(calendarioQuePermite);
    usuarioQueUsaSubteSpy.setMedioDeComunicacion(mailSender);
    entidad.reportarIncidente(servicio1, LocalDateTime.now(), "No anda la cadena");
    verify(usuarioQueUsaSubteSpy, never()).notificar(any());
  }

  @Test
  public void unUsuarioNoEsNotificadoPorLaAperturaDeUnIncidenteCuandoNoEsHorarioDisponible() {
    mailSender = mock(MailSender.class);
    usuarioQueUsaSubte.setMedioDeComunicacion(mailSender);
    usuarioQueUsaSubte.setCalendarioNotificaciones(calendarioQueNoPermite);
    entidad.agregarUsuarioInteresado(usuarioQueUsaSubte);

    entidad.reportarIncidente(servicio1, LocalDateTime.now(), "No anda la cadena");

    ArgumentCaptor<Notificacion> notificacionCaptor = ArgumentCaptor.forClass(Notificacion.class);

    // Verificar que el medio de comunicacion llega a procesar la notificacion y que el receptor es el correcto
    verifyNoInteractions(mailSender);
  }


}
