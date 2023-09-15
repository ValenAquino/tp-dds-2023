package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.rankings.criterios.MayorPromedioCierre;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MayoPromedioCierreTest {
  final Entidad entidadA = new Entidad("Entidad A", TipoDeEntidad.SUPERMERCADO);
  final Entidad entidadB = new Entidad("Entidad B", TipoDeEntidad.BANCO);
  final MayorPromedioCierre criterio = new MayorPromedioCierre();
  final Incidente incidente1 = mock(Incidente.class);
  final Incidente incidente2 = mock(Incidente.class);
  final Incidente incidente3 = mock(Incidente.class);

  @BeforeEach
  public void setUp() {
    when(incidente1.getEntidad()).thenReturn(entidadA);
    when(incidente2.getEntidad()).thenReturn(entidadB);
    when(incidente3.getEntidad()).thenReturn(entidadB);
  }

  private void assertOrdenEntidades(List<Incidente> incidentes, Entidad[] entidadesEsperadas) {
    Map<Entidad, Double> entidadesOrdenadas = criterio.getEntidadesOrdenadas(incidentes);

    entidadesOrdenadas.forEach((entidad, count) -> System.out.println("Entidad: " + entidad.getNombre() + ", Incidentes: " + count));

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
    when(incidente1.estaResuelto()).thenReturn(false);
    when(incidente2.estaResuelto()).thenReturn(false);
    when(incidente3.estaResuelto()).thenReturn(false);

    List<Incidente> incidentesAbiertos = List.of(incidente1, incidente2, incidente3);
    Map<Entidad, Double> resultado = criterio.getEntidadesOrdenadas(incidentesAbiertos);

    assertEquals(0, resultado.size());
  }

  public List<Incidente> entidadBtienePromedioDe2yEntidadAtienePromedio1() {
    when(incidente1.estaResuelto()).thenReturn(true);
    when(incidente2.estaResuelto()).thenReturn(true);
    when(incidente3.estaResuelto()).thenReturn(true);

    // Promedio de cierre de 1 día Entidad A
    when(incidente1.tiempoDeCierre()).thenReturn(Duration.ofDays(1).toMillis());

    // Promedio de cierre de 2 días Entidad B
    when(incidente2.tiempoDeCierre()).thenReturn(Duration.ofDays(2).toMillis());
    when(incidente3.tiempoDeCierre()).thenReturn(Duration.ofDays(2).toMillis());

    return List.of(incidente1, incidente2, incidente3);
  }

  public List<Incidente> entidadAtienePromedioDe2yEntidadBtienePromedio1() {
    when(incidente1.estaResuelto()).thenReturn(true);
    when(incidente2.estaResuelto()).thenReturn(true);
    when(incidente3.estaResuelto()).thenReturn(true);

    // Promedio de cierre de 2 día Entidad A
    when(incidente1.tiempoDeCierre()).thenReturn(Duration.ofDays(2).toMillis());

    // Promedio de cierre de 1 días Entidad B
    when(incidente2.tiempoDeCierre()).thenReturn(Duration.ofDays(1).toMillis());
    when(incidente3.tiempoDeCierre()).thenReturn(Duration.ofDays(1).toMillis());

    return List.of(incidente1, incidente2, incidente3);
  }

}
