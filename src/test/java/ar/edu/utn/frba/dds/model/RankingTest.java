package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.entidades.Entidad;
import ar.edu.utn.frba.dds.model.entidades.Establecimiento;
import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.Ubicacion;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.model.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.model.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.model.entidades.rankings.criterios.CantidadIncidentes;
import ar.edu.utn.frba.dds.model.entidades.rankings.criterios.MayorPromedioCierre;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioIncidentes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioNotificaciones;
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

  Ubicacion carabobo;
  Ubicacion flores;
  Ubicacion caballito;
  Ubicacion once;
  RepositorioIncidentes repo;
  RepositorioNotificaciones repositorioNotificaciones;
  RepositorioComunidades repositorioComunidades;

  @BeforeEach
  public void setUp() {
    setUpSubteA();
    setUpLineaSarmiento();

    repo = mock(RepositorioIncidentes.class);
    repositorioNotificaciones = mock(RepositorioNotificaciones.class);
    repositorioComunidades = mock(RepositorioComunidades.class);
    rankingMayorCantidadIncidentes = new Ranking(repo, new CantidadIncidentes());
    rankingMayorPromedioDeCierre = new Ranking(repo, new MayorPromedioCierre());
  }

  private void setUpSubteA() {
    carabobo = new Ubicacion(-34.626417, -58.456083);
    flores = new Ubicacion(-34.62861111, -58.46444444);

    subteA = new Entidad("subteA", TipoDeEntidad.SUBTERRANEO);

    ascensorALaCalle = new Servicio("ascensorALaCalle", TipoDeServicio.ASCENSORES);
    escaleraMecanica = new Servicio("escaleraMecanica", TipoDeServicio.ESCALERAS_MECANICAS);

    Establecimiento estacionCarabobo = new Establecimiento(subteA, carabobo);
    Establecimiento estacionFlores = new Establecimiento(subteA, flores);

    estacionCarabobo.agregarServicio(escaleraMecanica);
    estacionFlores.agregarServicio(ascensorALaCalle);

    ascensorALaCalle.setEstablecimiento(estacionFlores);
    escaleraMecanica.setEstablecimiento(estacionCarabobo);

    subteA.agregarEstablecimiento(estacionCarabobo);
    subteA.agregarEstablecimiento(estacionFlores);
  }

  private void setUpLineaSarmiento() {
    caballito = new Ubicacion(-34.6194, -58.4438);
    once = new Ubicacion(-34.608611111111, -58.408888888889);

    lineaSarmiento = new Entidad("lineaSarmiento", TipoDeEntidad.FERROCARRIL);

    Establecimiento estacionCaballito = new Establecimiento(lineaSarmiento, caballito);
    Establecimiento estacionOnce = new Establecimiento(lineaSarmiento, once);

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

    rankingMayorCantidadIncidentes.getEntidades().forEach((entidad, count) -> System.out.println("Entidad: " + entidad.getNombre() + ", Incidentes: " + count));

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

    rankingMayorPromedioDeCierre.getEntidades().forEach((entidad, count) -> System.out.println("Entidad: " + entidad.getNombre() + ", Incidentes: " + count));

    double tolerancia = 1e-7;// Tolerancia muy pequeña
    long valorEsperado = Duration.ofDays(1).toMillis();

    Assertions.assertEquals(
        valorEsperado,
        rankingMayorPromedioDeCierre.getEntidades().get(lineaSarmiento),
        valorEsperado * tolerancia
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

    incidente1.cerrar(LocalDateTime.now());
    incidente2.cerrar(LocalDateTime.now());

    // Incidentes de SubteA
    Incidente incidente3 = crearIncidente(
        escaleraMecanica, "escaleraMecanica", LocalDateTime.now().minusDays(1)
    );

    Incidente incidente4 = crearIncidente(
        ascensorALaCalle, "ascensorALaCalle", LocalDateTime.now().minusDays(1)
    );

    incidente3.cerrar(LocalDateTime.now());
    incidente4.cerrar(LocalDateTime.now());

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

    incidente1.cerrar(LocalDateTime.now());
    incidente2.cerrar(LocalDateTime.now());

    // Incidentes de SubteA
    Incidente incidente3 = crearIncidente(
        escaleraMecanica, "escaleraMecanica", LocalDateTime.now().minusDays(2)
    );

    Incidente incidente4 = crearIncidente(
        ascensorALaCalle, "ascensorALaCalle", LocalDateTime.now().minusDays(2)
    );

    incidente3.cerrar(LocalDateTime.now());
    incidente4.cerrar(LocalDateTime.now());

    return List.of(incidente1, incidente2, incidente3, incidente4);
  }

  public Incidente crearIncidente(Servicio s, String obs, LocalDateTime fecha) {
    Usuario usuario = new Usuario(
        "usr",
        "pw",
        "unNombre",
        "unApellido",
        "usr@mail",
        repositorioComunidades,
        repositorioNotificaciones
    );
    return new Incidente(s, obs, fecha, usuario);
  }
}