package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Establecimiento;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.entidades.rankings.criterios.MayorPromedioCierre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MayoPromedioCierreTest {
  Entidad entidadA = new Entidad("Entidad A", TipoDeEntidad.SUPERMERCADO);
  Entidad entidadB = new Entidad("Entidad B", TipoDeEntidad.BANCO);
  Servicio servicio1 = new Servicio("Servicio 1", TipoDeServicio.ESCALERAS_MECANICAS);
  Servicio servicio2 = new Servicio("Servicio 2", TipoDeServicio.BANIOS);
  Establecimiento establecimiento1 = new Establecimiento();
  Establecimiento establecimiento2 = new Establecimiento();
  Incidente incidente1 = new Incidente(servicio1, "Observación 1");
  Incidente incidente2 = new Incidente(servicio2, "Observación 2");
  Incidente incidente3 = new Incidente(servicio2, "Observación 3");
  MayorPromedioCierre criterio = new MayorPromedioCierre();

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

  private void assertOrdenEntidades(List<Incidente> incidentes, Entidad[] entidadesEsperadas) {
    Map<Entidad, Double> entidadesOrdenadas = criterio.getEntidadesOrdenadas(incidentes);

    entidadesOrdenadas.forEach((entidad, count) -> {
      System.out.println("Entidad: " + entidad.getNombre() + ", Incidentes: " + count);
    });

    int i = 0;
    for (Map.Entry<Entidad, Double> entry : entidadesOrdenadas.entrySet()) {
      // Compara el nombre de la entidad con el nombre de la entidad esperada en esa posición
      assertEquals(entidadesEsperadas[i].getNombre(), entry.getKey().getNombre());
      i++;
    }
  }

  @Test
  public void entidadBganaConPromedioDe2() {
    List<Incidente> entidadBpromedio2 = entidadBtienePromedioDe2yEntidadAtienePromedio1();
    Entidad[] entidadesEsperadas1 = {entidadB, entidadA};

    assertOrdenEntidades(entidadBpromedio2, entidadesEsperadas1);
  }

  @Test
  public void entidadAganaConPromedioDe2() {
    List<Incidente> entidadApromedio2 = entidadAtienePromedioDe2yEntidadBtienePromedio1();
    Entidad[] entidadesEsperadas2 = {entidadA, entidadB};

    assertOrdenEntidades(entidadApromedio2, entidadesEsperadas2);
  }

  @Test
  public void SinNoHayIncidentesCerradosElResultadoEsVacio() {
    List<Incidente> incidentesAbiertos = List.of(incidente1, incidente2, incidente3);
    Map<Entidad, Double> resultado = criterio.getEntidadesOrdenadas(incidentesAbiertos);

    assertEquals(0, resultado.size());
  }

  public List<Incidente> entidadBtienePromedioDe2yEntidadAtienePromedio1() {
    LocalDateTime fecha = LocalDateTime.now();
    // Promedio de cierre de 1 dia Entidad A
    incidente1.cerrar();
    incidente1.setFechaResolucion(fecha.plusDays(1));

    // Promedio de cierre de 2 dias Entidad B
    incidente2.cerrar();
    incidente3.cerrar();
    incidente2.setFechaResolucion(fecha.plusDays(2));
    incidente3.setFechaResolucion(fecha.plusDays(2));

    return List.of(incidente1, incidente2, incidente3);
  }

  public List<Incidente> entidadAtienePromedioDe2yEntidadBtienePromedio1() {
    LocalDateTime fecha = LocalDateTime.now();
    // Promedio de cierre de 2 dia Entidad A
    incidente1.cerrar();
    incidente1.setFechaResolucion(fecha.plusDays(2));

    // Promedio de cierre de 1 dias Entidad B
    incidente2.cerrar();
    incidente3.cerrar();
    incidente2.setFechaResolucion(fecha.plusDays(1));
    incidente3.setFechaResolucion(fecha.plusDays(1));

    return List.of(incidente1, incidente2, incidente3);
  }

}
