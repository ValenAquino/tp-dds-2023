package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.entidades.rankings.criterios.CantidadIncidentes;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CantidadIncidentesTest {
  Entidad entidadA = new Entidad("Entidad A", TipoDeEntidad.SUPERMERCADO);
  Entidad entidadB = new Entidad("Entidad B", TipoDeEntidad.BANCO);
  Servicio servicio1 = new Servicio("Servicio 1", TipoDeServicio.ESCALERAS_MECANICAS);
  Servicio servicio2 = new Servicio("Servicio 2", TipoDeServicio.BANIOS);
  Incidente incidente1 = mock(Incidente.class);
  Incidente incidente2 = mock(Incidente.class);
  Incidente incidente3 = mock(Incidente.class);

  @BeforeEach
  public void setUp() {
    when(incidente1.getEntidad()).thenReturn(entidadA);
    when(incidente1.getServicio()).thenReturn(servicio1);

    when(incidente2.getEntidad()).thenReturn(entidadB);
    when(incidente3.getEntidad()).thenReturn(entidadB);
    when(incidente2.getServicio()).thenReturn(servicio2);
    when(incidente3.getServicio()).thenReturn(servicio2);
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
    // entidadA = Estacion Pichincha
    // entidadB = Estacion Tribunales

    LocalDateTime cincoDeMayoAlas15 = LocalDateTime.of(2023, 5, 5, 15, 0);
    LocalDateTime cincoDeMayoAlas15y5 = LocalDateTime.of(2023, 5, 5, 15, 5);

    // Pasaron menos de 24 horas entre sí y no estan cerrados => cuentan como 1
    Incidente incidenteP1 = crearIncidente(entidadA, servicio1, cincoDeMayoAlas15); // "Pichincha 1"
    Incidente incidenteP2 = crearIncidente(entidadA, servicio1, cincoDeMayoAlas15y5); // "Pichincha 2"

    LocalDateTime cincoDeMayoAlas16 = LocalDateTime.of(2023, 5, 5, 16, 0);
    LocalDateTime cincoDeMayoAlas16y30 = LocalDateTime.of(2023, 5, 5, 16, 30);
    LocalDateTime cincoDeMayoAlas16y35 = LocalDateTime.of(2023, 5, 5, 16, 35);

    // Estos tres cuentan como 1 por lo mismo, se reportaron el mismo día y nunca se cerraron
    Incidente incidenteT1 = crearIncidente(entidadB, servicio2, cincoDeMayoAlas16); // "Tribunales 1"
    Incidente incidenteT2 = crearIncidente(entidadB, servicio2, cincoDeMayoAlas16y30); // "Tribunales 2"
    Incidente incidenteT3 = crearIncidente(entidadB, servicio2, cincoDeMayoAlas16y35); // "Tribunales 3"

    LocalDateTime cincoDeMayoAlas16y50 = LocalDateTime.of(2023, 5, 5, 16, 50);

    // Este cuentan como otro porque 5 minutos antes se cerró uno y se volvió a abrir
    Incidente incidenteT4 = crearIncidente(entidadB, servicio2, cincoDeMayoAlas16y50); // "Tribunales 4"

    LocalDateTime sieteDeMayoAlas8 = LocalDateTime.of(2023, 7, 5, 16, 0);

    // Este cuenta porque se abrio + de 24hs después que el anterior
    Incidente incidenteT5 = crearIncidente(entidadB, servicio2, sieteDeMayoAlas8); // "Tribunales 5"

    when(incidenteT2.estaResuelto()).thenReturn(true);
    when(incidenteT2.getFechaResolucion()).thenReturn(cincoDeMayoAlas16.plusMinutes(45));

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
    when(incidente1.estaResuelto()).thenReturn(true);
    when(incidente1.getFecha()).thenReturn(fecha);
    when(incidente1.getFechaResolucion()).thenReturn(fecha);

    // Los incidente 2 y 3 pertenecen a la entidad B
    when(incidente2.estaResuelto()).thenReturn(true);
    when(incidente2.getFecha()).thenReturn(fecha.plusMinutes(10));
    when(incidente2.getFechaResolucion()).thenReturn(fecha.plusMinutes(15));

    // Este se abre 5 minutos despues que se cierra el incidente anterior del mismo servicio
    // Por lo que deberia contar como otro incidente
    when(incidente3.estaResuelto()).thenReturn(false);
    when(incidente3.getFecha()).thenReturn(fecha.plusMinutes(20));

    return Arrays.asList(incidente1, incidente2, incidente3);
  }

  public List<Incidente> incidentesCaso2() {
    LocalDateTime fecha = LocalDateTime.now();

    // El incidente 1 Pertenece a la entidad A
    when(incidente1.estaResuelto()).thenReturn(true);
    when(incidente1.getFecha()).thenReturn(fecha);
    when(incidente1.getFechaResolucion()).thenReturn(fecha);

    // Los incidente 2 y 3 pertenecen a la entidad B
    when(incidente2.estaResuelto()).thenReturn(true);
    when(incidente2.getFecha()).thenReturn(fecha.plusMinutes(10));
    when(incidente2.getFechaResolucion()).thenReturn(fecha.plusMinutes(15));

    // Este se abre 1 minuto ANTES que se cierra el incidente anterior del mismo servicio
    // Por lo que NO deberia contar como otro incidente
    when(incidente3.estaResuelto()).thenReturn(false);
    when(incidente3.getFecha()).thenReturn(fecha.plusMinutes(14));

    return Arrays.asList(incidente1, incidente2, incidente3);
  }

  public List<Incidente> incidentesCaso3() {
    LocalDateTime fecha = LocalDateTime.now();

    // El incidente 1 Pertenece a la entidad A
    when(incidente1.estaResuelto()).thenReturn(true);
    when(incidente1.getFecha()).thenReturn(fecha);
    when(incidente1.getFechaResolucion()).thenReturn(fecha);

    // Los incidente 2 y 3 pertenecen a la entidad B
    when(incidente2.estaResuelto()).thenReturn(false);
    when(incidente2.getFecha()).thenReturn(fecha.plusMinutes(10));

    // Este se abre 24hs Despues que se abre el incidente anterior del mismo servicio
    // Por lo que deberia contar como otro incidente
    when(incidente3.estaResuelto()).thenReturn(false);
    when(incidente3.getFecha()).thenReturn(fecha.plusMinutes(10).plusHours(24));

    return Arrays.asList(incidente1, incidente2, incidente3);
  }

  public List<Incidente> incidentesCaso4() {
    LocalDateTime fecha = LocalDateTime.now();

    // El incidente 1 Pertenece a la entidad A
    when(incidente1.estaResuelto()).thenReturn(true);
    when(incidente1.getFecha()).thenReturn(fecha.plusMinutes(10));
    when(incidente1.getFechaResolucion()).thenReturn(fecha);

    // Los incidente 2 y 3 pertenecen a la entidad B
    when(incidente2.estaResuelto()).thenReturn(false);
    when(incidente2.getFecha()).thenReturn(fecha.plusMinutes(10));

    // Este se abre 10hs Despues que se abre el incidente anterior del mismo servicio
    // Por lo que NO deberia contar como otro incidente
    when(incidente3.estaResuelto()).thenReturn(false);
    when(incidente3.getFecha()).thenReturn(fecha.plusMinutes(10).plusHours(10));

    return Arrays.asList(incidente1, incidente2, incidente3);
  }

  private Incidente crearIncidente(Entidad entidad, Servicio servicio, LocalDateTime apertura) {
    Incidente incidente = mock(Incidente.class);
    when(incidente.getServicio()).thenReturn(servicio);
    when(incidente.getEntidad()).thenReturn(entidad);
    when(incidente.getFecha()).thenReturn(apertura);

    return incidente;
  }
}