package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Establecimiento;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.entidades.rankings.criterios.CantidadIncidentes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
public class CantidadIncidentesTest {
  Entidad entidadA = new Entidad("Entidad A", TipoDeEntidad.SUPERMERCADO);
  Entidad entidadB = new Entidad("Entidad B", TipoDeEntidad.BANCO);
  Servicio servicio1 = new Servicio("Servicio 1", TipoDeServicio.ESCALERAS_MECANICAS);
  Servicio servicio2 = new Servicio("Servicio 2", TipoDeServicio.BANIOS);
  Establecimiento establecimiento1 = new Establecimiento();
  Establecimiento establecimiento2 = new Establecimiento();
  Incidente incidente1 = new Incidente(servicio1, "Observación 1");
  Incidente incidente2 = new Incidente(servicio2, "Observación 2");
  Incidente incidente3 = new Incidente(servicio2, "Observación 3");

  @BeforeEach
  public void setUp() {
    entidadA.agregarEstablecimiento(establecimiento1);
    entidadB.agregarEstablecimiento(establecimiento2);

    establecimiento1.agregarServicio(servicio1);
    establecimiento2.agregarServicio(servicio2);

    establecimiento1.setEntidad(entidadA);
    establecimiento2.setEntidad(entidadB);

    servicio1.setEstablecimiento(establecimiento1);
    servicio2.setEstablecimiento(establecimiento2);
  }

  public void testCantidadIncidentes(List<Incidente> incidentes, double resA, double resB) {
    CantidadIncidentes cantidadIncidentes = new CantidadIncidentes();
    Map<Entidad, Double> resultados = cantidadIncidentes.getEntidadesOrdenadas(incidentes);

    resultados.forEach((entidad, count) -> {
      System.out.println("Entidad: " + entidad.getNombre() + ", Incidentes: " + count);
    });

    Assertions.assertEquals(resA, resultados.get(entidadA));
    Assertions.assertEquals(resB, resultados.get(entidadB));
  }

  @Test
  public void siSeCierraUnIncidenteYseAbreOtroDespuesEntoncesHayDosIncidentes() {
    testCantidadIncidentes(incidentesCaso1(), 1.0, 2.0);
  }

  @Test
  public void siSeCierraUnIncidenteYseAbreOtroAntesEntoncesHayUnIncidente() {
    testCantidadIncidentes(incidentesCaso2(), 1.0, 1.0);
  }

  @Test
  public void siSeAbreUnIncidenteDespuesDe24HorasQueAbrioOtroEntoncesHayDosIncidentes() {
    testCantidadIncidentes(incidentesCaso3(), 1.0, 2.0);
  }

  @Test
  public void siSeAbreUnIncidenteAntesDe24HorasQueAbrioOtroEntoncesHayDosIncidentes() {
    testCantidadIncidentes(incidentesCaso4(), 1.0, 1.0);
  }

  public List<Incidente> incidentesCaso1() {
    LocalDateTime fecha = LocalDateTime.now();

    // El incidente 1 Pertenece a la entidad A
    incidente1.cerrar();

    // Los incidente 2 y 3 pertenecen a la entidad B
    incidente2.cerrar();
    incidente2.setFechaApertura(fecha.plusMinutes(10));
    incidente2.setFechaResolucion(fecha.plusMinutes(15));

    // Este se abre 5 minutos despues que se cierra el incidente anterior del mismo servicio
    // Por lo que deberia contar como otro incidente
    incidente3.setFechaApertura(fecha.plusMinutes(20));

    return Arrays.asList(incidente1, incidente2, incidente3);
  }

  public List<Incidente> incidentesCaso2() {
    LocalDateTime fecha = LocalDateTime.now();

    // El incidente 1 Pertenece a la entidad A
    incidente1.cerrar();

    // Los incidente 2 y 3 pertenecen a la entidad B
    incidente2.cerrar();
    incidente2.setFechaApertura(fecha.plusMinutes(10));
    incidente2.setFechaResolucion(fecha.plusMinutes(15));

    // Este se abre 1 minuto ANTES que se cierra el incidente anterior del mismo servicio
    // Por lo que NO deberia contar como otro incidente
    incidente3.setFechaApertura(fecha.plusMinutes(14));

    return Arrays.asList(incidente1, incidente2, incidente3);
  }

  public List<Incidente> incidentesCaso3() {
    LocalDateTime fecha = LocalDateTime.now();

    // El incidente 1 Pertenece a la entidad A
    incidente1.cerrar();

    // Los incidente 2 y 3 pertenecen a la entidad B
    incidente2.setFechaApertura(fecha.plusMinutes(10));

    // Este se abre 24hs Despues que se abre el incidente anterior del mismo servicio
    // Por lo que deberia contar como otro incidente
    incidente3.setFechaApertura(fecha.plusMinutes(10).plusHours(24));

    return Arrays.asList(incidente1, incidente2, incidente3);
  }

  public List<Incidente> incidentesCaso4() {
    LocalDateTime fecha = LocalDateTime.now();

    // El incidente 1 Pertenece a la entidad A
    incidente1.cerrar();

    // Los incidente 2 y 3 pertenecen a la entidad B
    incidente2.setFechaApertura(fecha.plusMinutes(10));

    // Este se abre 10hs Despues que se abre el incidente anterior del mismo servicio
    // Por lo que NO deberia contar como otro incidente
    incidente3.setFechaApertura(fecha.plusMinutes(10).plusHours(10));

    return Arrays.asList(incidente1, incidente2, incidente3);
  }
}