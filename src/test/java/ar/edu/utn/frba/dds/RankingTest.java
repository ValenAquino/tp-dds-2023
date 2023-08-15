//package ar.edu.utn.frba.dds;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doAnswer;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import ar.edu.utn.frba.dds.entidades.Entidad;
//import ar.edu.utn.frba.dds.entidades.Establecimiento;
//import ar.edu.utn.frba.dds.entidades.Incidente;
//import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
//import ar.edu.utn.frba.dds.entidades.Servicio;
//import ar.edu.utn.frba.dds.entidades.Usuario;
//import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
//import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
//import ar.edu.utn.frba.dds.entidades.rankings.CriterioDeOrdenamiento;
//import ar.edu.utn.frba.dds.entidades.rankings.Ranking;
//import ar.edu.utn.frba.dds.entidades.rankings.criterios.CantidadIncidentes;
//import ar.edu.utn.frba.dds.entidades.rankings.criterios.MayorPromedioCierre;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioIncidentes;
//import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
//import ar.edu.utn.frba.dds.notificaciones.medios.MailSender;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class RankingTest {
//  private Entidad subteA;
//  private Entidad lineaSarmiento;
//  private Establecimiento estacionFlores;
//  private Establecimiento estacionCarabobo;
//  private Establecimiento estacionCaballito;
//  private Establecimiento estacionOnce;
//  private Servicio banioDeMujeres;
//  private Servicio ascensorALaCalle;
//  private Servicio escaleraMecanica;
//  private Servicio banioDeHombres;
//
//  private Ranking rankingMayorPromedioDeCierre;
//  private Ranking rankingMayorCantidadIncidentes;
//
//  private CantidadIncidentes criterioCantidadIncidentes;
//  private MayorPromedioCierre criterioMayorPromedioCierre;
//
//  @BeforeEach
//  public void setUp() {
//    subteA = new Entidad("subteA", TipoDeEntidad.SUBTERRANEO);
//    lineaSarmiento = new Entidad("lineaSarmiento", TipoDeEntidad.FERROCARRIL);
//
//    banioDeMujeres = new Servicio("banioDeMujeres", TipoDeServicio.BANIOS);
//    ascensorALaCalle = new Servicio("ascensorALaCalle", TipoDeServicio.ASCENSORES);
//    escaleraMecanica = new Servicio("escaleraMecanica", TipoDeServicio.ESCALERAS_MECANICAS);
//    banioDeHombres = new Servicio("banioDeHombres", TipoDeServicio.BANIOS);
//
//    estacionFlores = new Establecimiento();
//    estacionCarabobo = new Establecimiento();
//    estacionCaballito = new Establecimiento();
//    estacionOnce = new Establecimiento();
//
//    estacionFlores.agregarServicio(ascensorALaCalle);
//    estacionCaballito.agregarServicio(banioDeHombres);
//    estacionCarabobo.agregarServicio(escaleraMecanica);
//    estacionOnce.agregarServicio(banioDeMujeres);
//
//    subteA.agregarEstablecimiento(estacionCarabobo);
//    subteA.agregarEstablecimiento(estacionFlores);
//
//    lineaSarmiento.agregarEstablecimiento(estacionCaballito);
//    lineaSarmiento.agregarEstablecimiento(estacionOnce);
//
//    criterioCantidadIncidentes = mock(CantidadIncidentes.class);
//    criterioMayorPromedioCierre = mock(MayorPromedioCierre.class);
//
//    rankingMayorCantidadIncidentes = new Ranking(criterioCantidadIncidentes);
//    rankingMayorPromedioDeCierre = new Ranking(criterioMayorPromedioCierre);
//
//  }
//  @Test
//  public void elRankingPorMayorCantidadDeIncidentesDevuelveLaListaEnElOrdenCorrecto(){
//
//    Incidente seRompeLaCadenaEnEstacionCaballito = lineaSarmiento.abrirIncidente(banioDeHombres,"No anda la cadena");
//    Incidente seRompeLaCanillaEnEstacionOnce = lineaSarmiento.abrirIncidente(banioDeMujeres,"No anda la canilla");
//
//    Incidente seRompeElAscensor = subteA.abrirIncidente(ascensorALaCalle,"No sube el ascensor");
//
//    seRompeLaCadenaEnEstacionCaballito.cerrar();
//    seRompeLaCanillaEnEstacionOnce.cerrar();
//    seRompeElAscensor.cerrar();
//
//    Map<Entidad,Double> entidadesOrdenadasEsperadas = new HashMap<>();
//    entidadesOrdenadasEsperadas.put(lineaSarmiento,(double)2);
//    entidadesOrdenadasEsperadas.put(subteA,(double)1);
//
//    when(criterioCantidadIncidentes.getEntidadesOrdenadas(any())).thenReturn(entidadesOrdenadasEsperadas);
//
//    rankingMayorCantidadIncidentes.generarRanking();
//
//    Assertions.assertEquals(rankingMayorCantidadIncidentes.getEntidades().size(),2);
//    Assertions.assertEquals(rankingMayorCantidadIncidentes.getEntidades().keySet(),new HashSet<>(Arrays.asList(lineaSarmiento,subteA)));
//  }
//  @Test
//  public void elRankingPorMayorPromedioDeCierreDeIncidentesDevuelveLaListaEnElOrdenCorrecto(){
//
//
//    Incidente seRompeLaCadenaEnEstacionCaballito = mock(Incidente.class);
//
//    Incidente seRompeLaCanillaEnEstacionOnce = mock(Incidente.class);
//
//    Incidente seRompeElAscensor = mock(Incidente.class);
//
//    when(seRompeLaCadenaEnEstacionCaballito.getFechaResolucion()).thenReturn(LocalDateTime.of(2023,7,8,10,0,0));
//    when(seRompeLaCanillaEnEstacionOnce.getFechaResolucion()).thenReturn(LocalDateTime.of(2023,7,8,11,0,0));
//    when(seRompeElAscensor.getFechaResolucion()).thenReturn(LocalDateTime.of(2023,7,7,23,50,0));
//
//
//    seRompeLaCadenaEnEstacionCaballito.cerrar();
//    seRompeLaCanillaEnEstacionOnce.cerrar();
//    seRompeElAscensor.cerrar();
//
//    Map<Entidad,Double> entidadesOrdenadasEsperadas = new HashMap<>();
//    entidadesOrdenadasEsperadas.put(subteA,(double)1);
//    entidadesOrdenadasEsperadas.put(lineaSarmiento,(double)2);
//
//    when(criterioMayorPromedioCierre.getEntidadesOrdenadas(any())).thenReturn(entidadesOrdenadasEsperadas);
//
//    rankingMayorPromedioDeCierre.generarRanking();
//
//    Assertions.assertEquals(rankingMayorPromedioDeCierre.getEntidades().size(),2);
//    Assertions.assertEquals(rankingMayorPromedioDeCierre.getEntidades().keySet(),new HashSet<>(Arrays.asList(subteA,lineaSarmiento)));
//  }
//}