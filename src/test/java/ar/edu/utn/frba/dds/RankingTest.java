package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Establecimiento;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.entidades.rankings.criterios.CantidadIncidentes;
import ar.edu.utn.frba.dds.entidades.rankings.criterios.MayorPromedioCierre;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioIncidentes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RankingTest {
  Entidad subteA;
  Entidad lineaSarmiento;
  Ranking rankingMayorPromedioDeCierre;
  Ranking rankingMayorCantidadIncidentes;
  Servicio banioDeMujeres;
  Servicio ascensorALaCalle;
  Servicio escaleraMecanica;
  Servicio banioDeHombres;
  RepositorioIncidentes repo;

  @BeforeEach
  public void setUp() {
    setUpSubteA();
    setUpLineaSarmiento();

    repo = mock(RepositorioIncidentes.class);
    rankingMayorCantidadIncidentes = new Ranking(repo, new CantidadIncidentes());
    rankingMayorPromedioDeCierre = new Ranking(repo, new MayorPromedioCierre());
  }

  private void setUpSubteA() {
    subteA = new Entidad("subteA", TipoDeEntidad.SUBTERRANEO);

    ascensorALaCalle = new Servicio("ascensorALaCalle", TipoDeServicio.ASCENSORES);
    escaleraMecanica = new Servicio("escaleraMecanica", TipoDeServicio.ESCALERAS_MECANICAS);

    Establecimiento estacionCarabobo = new Establecimiento(subteA);
    Establecimiento estacionFlores = new Establecimiento(subteA);

    estacionCarabobo.agregarServicio(escaleraMecanica);
    estacionFlores.agregarServicio(ascensorALaCalle);

    ascensorALaCalle.setEstablecimiento(estacionFlores);
    escaleraMecanica.setEstablecimiento(estacionCarabobo);

    subteA.agregarEstablecimiento(estacionCarabobo);
    subteA.agregarEstablecimiento(estacionFlores);
  }

  private void setUpLineaSarmiento() {
    lineaSarmiento = new Entidad("lineaSarmiento", TipoDeEntidad.FERROCARRIL);

    Establecimiento estacionCaballito = new Establecimiento(lineaSarmiento);
    Establecimiento estacionOnce = new Establecimiento(lineaSarmiento);

    banioDeMujeres = new Servicio("banioDeMujeres", TipoDeServicio.BANIOS);
    banioDeHombres = new Servicio("banioDeHombres", TipoDeServicio.BANIOS);

    estacionCaballito.agregarServicio(banioDeHombres);
    estacionOnce.agregarServicio(banioDeMujeres);

    banioDeHombres.setEstablecimiento(estacionCaballito);
    banioDeMujeres.setEstablecimiento(estacionOnce);

    lineaSarmiento.agregarEstablecimiento(estacionCaballito);
    lineaSarmiento.agregarEstablecimiento(estacionOnce);
  }

  @Test
  public void elRankingPorMayorCantidadDeIncidentesDevuelveLaListaEnElOrdenCorrecto() {
    List<Incidente> incidentes = getIncidentes();
    when(repo.ultimaSemana()).thenReturn(incidentes);

    rankingMayorCantidadIncidentes.generarRanking();

    rankingMayorCantidadIncidentes.getEntidades().forEach((entidad, count) -> {
      System.out.println("Entidad: " + entidad.getNombre() + ", Incidentes: " + count);
    });

    Assertions.assertEquals(
        2, rankingMayorCantidadIncidentes.getEntidades().get(lineaSarmiento)
    );

    Assertions.assertEquals(
        2, rankingMayorCantidadIncidentes.getEntidades().get(subteA)
    );
  }

  @Test
  public void elRankingPorMayorPromedioDeCierreDeIncidentesDevuelveLaListaEnElOrdenCorrecto() {
    List<Incidente> incidentes = getIncidentesConPromedio1y2();
    when(repo.ultimaSemana()).thenReturn(incidentes);

    rankingMayorPromedioDeCierre.generarRanking();

    rankingMayorPromedioDeCierre.getEntidades().forEach((entidad, count) -> {
      System.out.println("Entidad: " + entidad.getNombre() + ", Incidentes: " + count);
    });

    Assertions.assertEquals(
        Duration.ofDays(1).toMillis(),
        rankingMayorPromedioDeCierre.getEntidades().get(lineaSarmiento)
    );

    Assertions.assertEquals(
        Duration.ofDays(2).toMillis(),
        rankingMayorPromedioDeCierre.getEntidades().get(subteA)
    );
  }

  public List<Incidente> getIncidentes() {
    // Incidentes de LineaSarmiento
    Incidente incidente1 = new Incidente(banioDeHombres, "banioDeHombres");
    Incidente incidente2 = new Incidente(banioDeMujeres, "banioDeMujeres");

    incidente1.setFechaApertura(LocalDateTime.now().minusDays(1));
    incidente2.setFechaApertura(LocalDateTime.now().minusDays(1));

    incidente1.cerrar();
    incidente2.cerrar();

    // Incidentes de SubteA
    Incidente incidente3 = new Incidente(escaleraMecanica, "escaleraMecanica");
    Incidente incidente4 = new Incidente(ascensorALaCalle, "ascensorALaCalle");

    incidente3.cerrar();
    incidente4.cerrar();

    incidente3.setFechaApertura(LocalDateTime.now().minusDays(1));
    incidente4.setFechaApertura(LocalDateTime.now().minusDays(1));

    return List.of(incidente1, incidente2, incidente3, incidente4);
  }

  public List<Incidente> getIncidentesConPromedio1y2() {
    LocalDateTime fecha = LocalDateTime.now();

    // Incidentes de LineaSarmiento
    Incidente incidente1 = new Incidente(banioDeHombres, "banioDeHombres");
    incidente1.cerrar();

    Incidente incidente2 = new Incidente(banioDeMujeres, "banioDeMujeres");
    incidente2.cerrar();

    incidente1.setFechaApertura(LocalDateTime.now().minusDays(1));
    incidente2.setFechaApertura(LocalDateTime.now().minusDays(1));

    // Incidentes de SubteA
    Incidente incidente3 = new Incidente(escaleraMecanica, "escaleraMecanica");
    incidente3.cerrar();

    Incidente incidente4 = new Incidente(ascensorALaCalle, "ascensorALaCalle");
    incidente4.cerrar();

    incidente3.setFechaApertura(LocalDateTime.now().minusDays(2));
    incidente4.setFechaApertura(LocalDateTime.now().minusDays(2));

    return List.of(incidente1, incidente2, incidente3, incidente4);
  }
}