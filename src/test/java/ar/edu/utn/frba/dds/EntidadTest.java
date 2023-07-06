package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Establecimiento;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.TipoDeServicio;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntidadTest {
  private Entidad entidad;
  private Establecimiento establecimiento1;
  private Establecimiento establecimiento2;
  private Servicio servicio1;
  private Servicio servicio2;

  @BeforeEach
  public void setUp() {
    entidad = new Entidad("entidad", TipoDeEntidad.SUBTERRANEO);
    establecimiento1 = new Establecimiento();
    establecimiento2 = new Establecimiento();
    servicio1 = new Servicio("servicioInestable", TipoDeServicio.BANIOS);
    servicio2 = new Servicio("ascensorInestable", TipoDeServicio.ASCENSORES);

    entidad.agregarEstablecimiento(establecimiento1);
    entidad.agregarEstablecimiento(establecimiento2);

    establecimiento1.agregarServicio(servicio1);
    establecimiento2.agregarServicio(servicio2);
  }

  @Test
  public void unaEntidadConoceLosIncidentesEnSusEstablecimientos() {
    Incidente incidente1 = new Incidente(servicio1, "No anda la cadena");
    Incidente incidente2 = new Incidente(servicio2, "No anda el ascensor");

    servicio1.agregarIncidente(incidente1);
    servicio2.agregarIncidente(incidente2);

    Assertions.assertEquals(2, entidad.getIncidentes().size());
    Assertions.assertTrue(entidad.getIncidentes().contains(incidente1));
    Assertions.assertTrue(entidad.getIncidentes().contains(incidente2));
  }

  @Test public void seObtienenLosIncidentesDeLaUltimaSemana() {
    LocalDateTime fechaActual = LocalDateTime.now();
    LocalDateTime fechaLimite = fechaActual.minusDays(7);

    Incidente incidente1 = new Incidente(servicio1, "Incidente1");
    incidente1.setFecha(fechaLimite.plusDays(1)); // 6 dias atras
    servicio1.agregarIncidente(incidente1);

    Incidente incidente2 = new Incidente(servicio2, "Incidente2");
    incidente2.setFecha(fechaLimite.minusDays(1)); // 8 dias atras
    servicio2.agregarIncidente(incidente2);

    Incidente incidente3 = new Incidente(servicio1, "Incidente3");
    incidente3.setFecha(fechaLimite.plusDays(3)); // 4 dias atras
    servicio1.agregarIncidente(incidente3);

    Incidente incidente4 = new Incidente(servicio2, "Incidente2");
    incidente4.setFecha(fechaActual.plusDays(1)); // 1 dia despues
    servicio2.agregarIncidente(incidente4);

    List<Incidente> incidentesUltimaSemana = entidad.getIncidentesSemana(fechaActual);

    Assertions.assertEquals(2, incidentesUltimaSemana.size());
    Assertions.assertTrue(incidentesUltimaSemana.contains(incidente1));
    Assertions.assertFalse(incidentesUltimaSemana.contains(incidente2));
    Assertions.assertTrue(incidentesUltimaSemana.contains(incidente3));
    Assertions.assertFalse(incidentesUltimaSemana.contains(incidente4));
  }

}
