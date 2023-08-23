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

  public void assertCantidadIncidentes(List<Incidente> incidentes, double resA, double resB) {
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
    assertCantidadIncidentes(incidentesCaso1(), 1.0, 2.0);
  }

  @Test
  public void siSeCierraUnIncidenteYseAbreOtroAntesEntoncesHayUnIncidente() {
    assertCantidadIncidentes(incidentesCaso2(), 1.0, 1.0);
  }

  @Test
  public void siSeAbreUnIncidenteDespuesDe24HorasQueAbrioOtroEntoncesHayDosIncidentes() {
    assertCantidadIncidentes(incidentesCaso3(), 1.0, 2.0);
  }

  @Test
  public void siSeAbreUnIncidenteAntesDe24HorasQueAbrioOtroEntoncesHayDosIncidentes() {
    assertCantidadIncidentes(incidentesCaso4(), 1.0, 1.0);
  }

  @Test
  public void seCumpleElCasoQueFrancoPlanteoEnElMail() {
    assertCantidadIncidentes(incidentesCasoFranco(), 1.0, 3.0);
  }

  public List<Incidente> incidentesCasoFranco() {
    // servicio1 = Estacion Pichincha
    // servicio2 = Estacion Tribunales

    LocalDateTime cincoDeMayoAlas15 = LocalDateTime.of(2023, 5, 5, 15, 0);
    Incidente incidenteP1 = new Incidente(servicio1, "Pichincha 1");
    Incidente incidenteP2 = new Incidente(servicio1, "Pichincha 2");

    // Pasaron menos de 24 horas entre sí y no estan cerrados => cuentan como 1
    incidenteP1.setFechaApertura(cincoDeMayoAlas15);
    incidenteP2.setFechaApertura(cincoDeMayoAlas15.plusMinutes(5));

    LocalDateTime cincoDeMayoAlas16 = LocalDateTime.of(2023, 5, 5, 16, 0);
    Incidente incidenteT1 = new Incidente(servicio2, "Tribunales 1");
    Incidente incidenteT2 = new Incidente(servicio2, "Tribunales 2");
    Incidente incidenteT3 = new Incidente(servicio2, "Tribunales 3");

    // Estos tres cuentan como 1 por lo mismo, se reportaron el mismo día y nunca se cerraron
    incidenteT1.setFechaApertura(cincoDeMayoAlas16);
    incidenteT2.setFechaApertura(cincoDeMayoAlas16.plusMinutes(30));
    incidenteT3.setFechaApertura(cincoDeMayoAlas16.plusMinutes(35));

    LocalDateTime sieteDeMayoAlas8 = LocalDateTime.of(2023, 7, 5, 16, 0);
    Incidente incidenteT4 = new Incidente(servicio2, "Tribunales 4");
    Incidente incidenteT5 = new Incidente(servicio2, "Tribunales 5");

    incidenteT2.cerrar();
    incidenteT2.setFechaResolucion(cincoDeMayoAlas16.plusMinutes(45));

    // Este cuentan como otro porque 5 minutos antes se cerró uno y se volvió a abrir
    incidenteT4.setFechaApertura(cincoDeMayoAlas16.plusMinutes(50));
    // Este cuenta porque se abrio + de 24hs después que el anterior
    incidenteT5.setFechaApertura(sieteDeMayoAlas8);

    return Arrays.asList(
        incidenteP1, incidenteP2, incidenteT1, incidenteT2, incidenteT3, incidenteT4, incidenteT5
    );
  }

//  @Test
//  public void siNoHayNingunIncidenteDevuelveCero() {
//    CantidadIncidentes cantidadIncidentes = new CantidadIncidentes();
//    List<Incidente> incidentes = List.of(/*Vacio*/);
//    Map<Entidad, Double> resultados = cantidadIncidentes.getEntidadesOrdenadas(incidentes);
//
//    Esto Rompería, hace falta que lo contemplemos?
//    - Ranking Exception (No hay incidentes para agrupar)
//  }

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