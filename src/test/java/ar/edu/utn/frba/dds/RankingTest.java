package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Establecimiento;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.entidades.rankings.criterios.CantidadIncidentes;
import ar.edu.utn.frba.dds.entidades.rankings.criterios.MayorPromedioCierre;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioIncidentes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    Incidente incidente1 = crearIncidente(
        banioDeHombres, "banioDeHombres", LocalDateTime.now().minusDays(1)
    );

    Incidente incidente2 = crearIncidente(
        banioDeMujeres, "banioDeMujeres", LocalDateTime.now().minusDays(1)
    );

    incidente1.cerrar();
    incidente2.cerrar();

    // Incidentes de SubteA
    Incidente incidente3 = crearIncidente(
        escaleraMecanica, "escaleraMecanica", LocalDateTime.now().minusDays(1)
    );

    Incidente incidente4 = crearIncidente(
        ascensorALaCalle, "ascensorALaCalle", LocalDateTime.now().minusDays(1)
    );

    incidente3.cerrar();
    incidente4.cerrar();

    return List.of(incidente1, incidente2, incidente3, incidente4);
  }

  public List<Incidente> getIncidentesConPromedio1y2() {
    LocalDateTime fecha = LocalDateTime.now();

    // Incidentes de LineaSarmiento
    Incidente incidente1 = crearIncidente(
        banioDeHombres, "banioDeHombres", fecha.minusDays(1)
    );

    Incidente incidente2 = crearIncidente(
        banioDeMujeres, "banioDeMujeres", fecha.minusDays(1)
    );

    incidente1.cerrar();
    incidente2.cerrar();

    // Incidentes de SubteA
    Incidente incidente3 = crearIncidente(
        escaleraMecanica, "escaleraMecanica", LocalDateTime.now().minusDays(2)
    );

    Incidente incidente4 = crearIncidente(
        ascensorALaCalle, "ascensorALaCalle", LocalDateTime.now().minusDays(2)
    );

    incidente3.cerrar();
    incidente4.cerrar();

    return List.of(incidente1, incidente2, incidente3, incidente4);
  }

  public Incidente crearIncidente(Servicio s, String obs, LocalDateTime fecha) {
    Usuario usuario = new Usuario("usr", "pw", "unNombre", "unApellido", "usr@mail");
    return new Incidente(s, obs, fecha, usuario);
  }
}