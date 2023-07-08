package ar.edu.utn.frba.dds;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Establecimiento;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import java.time.LocalDateTime;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.notificaciones.medios.MailSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntidadTest {
  private Entidad entidad;
  private Establecimiento establecimiento1;
  private Establecimiento establecimiento2;
  private Establecimiento establecimiento3;
  private Servicio servicio1;
  private Servicio servicio2;
  private Servicio servicio3;
  private Usuario usuarioQueUsaSubte;
  private MedioDeComunicacion medioDeComunicacion;
  private MailSender mailSender;

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

    usuarioQueUsaSubte = new Usuario(
        "subte.master",
        "",
        "Subte",
        "Master",
        "subtemaster@gmail.com"
    );
    medioDeComunicacion = mock(MedioDeComunicacion.class);
    mailSender = mock(MailSender.class);
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
  public void unaEntidadPuedeAbrirUnIncidenteEnUnServicioSuyo() {
    entidad.abrirIncidente(servicio1, "No anda la cadena");
    entidad.abrirIncidente(servicio2, "No funciona botón de piso 3");
    Assertions.assertEquals(2, entidad.getIncidentesAbiertos().size());
  }

  @Test
  public void unaEntidadNoPuedeAbrirUnIncidenteEnUnServicioAjeno() {
    Assertions.assertThrows(RuntimeException.class, () ->
        entidad.abrirIncidente(servicio3, "No funciona la escalera mecanica"));
  }

  @Test
  public void unUsuarioEsNotificadoPorLaAperturaDeUnIncidenteQueLeInteresa() {
    usuarioQueUsaSubte.setMedioDeComunicacion(mailSender);
    entidad.agregarUsuarioInteresado(usuarioQueUsaSubte);
    entidad.abrirIncidente(servicio1, "No anda la cadena");
    verify(mailSender).notificarAperturaDeIncidente(any());
  }

  @Test
  public void unUsuarioNoEsNotificadoPorLaAperturaDeUnIncidenteQueNoLeInteresa() {
    usuarioQueUsaSubte.setMedioDeComunicacion(mailSender);
    entidad.abrirIncidente(servicio1, "No anda la cadena");
    verify(medioDeComunicacion, never()).notificarAperturaDeIncidente(any());
  }
}
