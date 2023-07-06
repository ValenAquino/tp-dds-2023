package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Establecimiento;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.TipoDeServicio;
import ar.edu.utn.frba.dds.entidades.rankings.GeneradorRankingSemanal;
import ar.edu.utn.frba.dds.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.entidades.rankings.criterios.CantidadIncidentes;
import ar.edu.utn.frba.dds.entidades.rankings.criterios.MayorPromedioCierre;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GeneradorRankingSemanalTest {
  GeneradorRankingSemanal generador;
  OrganismoDeControl organismo;
  Entidad entidadA;
  Entidad entidadB;
  Entidad entidadC;

  @BeforeEach
  public void init() {
    organismo = new OrganismoDeControl("Organismo", "org@mail");
    generador = new GeneradorRankingSemanal(organismo);

    entidadA = crearEntidadConIncidentes(2, "Entidad A");
    entidadB = crearEntidadConIncidentes(3, "Entidad B");
    entidadC = crearEntidadConIncidentes(1, "Entidad C");

    organismo.agregarEntidad(entidadA);
    organismo.agregarEntidad(entidadB);
    organismo.agregarEntidad(entidadC);
  }

  @Test
  public void seGeneranRankingsOrdenadosPorCantidadIncidentes() {
    CantidadIncidentes cantidadIncidentes = new CantidadIncidentes();
    generador.agregarCriterio(cantidadIncidentes);

    // Prueba
    generador.generarRankingSemanal();
    List<Ranking> rankings = generador.getRankings();
    List<Entidad> entidadesOrdenadas = rankings.get(0).getEntidades();

    Assertions.assertEquals(entidadB, entidadesOrdenadas.get(0));
    Assertions.assertEquals(entidadA, entidadesOrdenadas.get(1));
    Assertions.assertEquals(entidadC, entidadesOrdenadas.get(2));
  }

  @Test
  public void ordenarEntidadesPorPromedioDeCierre() {
    MayorPromedioCierre mayorPromedioCierre = new MayorPromedioCierre();
    generador.agregarCriterio(mayorPromedioCierre);

    // Prueba
    generador.generarRankingSemanal();
    List<Ranking> rankings = generador.getRankings();
    List<Entidad> entidadesOrdenadas = rankings.get(0).getEntidades();

    Assertions.assertEquals(entidadB, entidadesOrdenadas.get(0));
    Assertions.assertEquals(entidadA, entidadesOrdenadas.get(1));
    Assertions.assertEquals(entidadC, entidadesOrdenadas.get(2));
  }

  private Entidad crearEntidadConIncidentes(int cantidadIncidentes, String nombre) {
    Entidad unaEntidad = new Entidad(nombre, TipoDeEntidad.SUBTERRANEO);
    Establecimiento unEstablecimiento = new Establecimiento();
    Servicio unBanio = new Servicio("servicioInestable", TipoDeServicio.BANIOS);

    LocalDateTime fechaApertura = LocalDateTime.now().minusDays(6);

    for (int i = 0; i < cantidadIncidentes; i++) {
      Incidente incidente = crearIncidenteEnBanios(unBanio, fechaApertura, i);
      unBanio.agregarIncidente(incidente);
    }

    unaEntidad.agregarEstablecimiento(unEstablecimiento);
    unEstablecimiento.agregarServicio(unBanio);

    return unaEntidad;
  }

  private Incidente crearIncidenteEnBanios(Servicio servicio, LocalDateTime apertura, int index) {
    // Cada incidente se cierra 2 horas despues del anterior
    // Cuantos mas incidentes, mas bajo sera el promedio de cierre
    LocalDateTime fechaCierre = apertura.plusHours(index * 2L);
    Incidente incidente = new Incidente(servicio, "No anda la cadena del baño - " + index);

    incidente.cerrar();
    incidente.setFecha(apertura);
    incidente.setFechaResolucion(fechaCierre);

    return incidente;
  }

}
