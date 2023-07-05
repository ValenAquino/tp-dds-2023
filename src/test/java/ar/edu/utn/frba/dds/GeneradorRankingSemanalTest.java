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
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GeneradorRankingSemanalTest {

  @Test
  public void seGeneranRankingsOrdenadosPorCantidadIncidentes() {
    OrganismoDeControl organismo = unOrganismoDeControlConTresIncidentes();
    CantidadIncidentes cantidadIncidentes = new CantidadIncidentes("CantidadIncidentes");
    GeneradorRankingSemanal generadorRankingSemanal = new GeneradorRankingSemanal(organismo);

    generadorRankingSemanal.agregarCriterio(cantidadIncidentes);
    generadorRankingSemanal.generarRankingSemanal();

    List<Ranking> rankings = generadorRankingSemanal.getRankings();
    List<Entidad> entidades = rankings.get(0).getEntidades();

    Assertions.assertEquals("b", entidades.get(0).getNombre());
    Assertions.assertEquals(1, generadorRankingSemanal.getRankings().size());
  }

  @Test
  public void ordenarEntidadesPorPromedioDeCierre() {
    MayorPromedioCierre mayorPromedioCierre = new MayorPromedioCierre("Mayor promedio de cierre");
    OrganismoDeControl organismoDeControl = new OrganismoDeControl("Organismo", "organismo@mail.com");

    Entidad entidadA = entidadConIncidentes(2, "Entidad A");
    Entidad entidadB = entidadConIncidentes(3, "Entidad B");
    Entidad entidadC = entidadConIncidentes(1, "Entidad C");

    organismoDeControl.agregarEntidad(entidadA);
    organismoDeControl.agregarEntidad(entidadB);
    organismoDeControl.agregarEntidad(entidadC);

    List<Entidad> entidades = new ArrayList<>();
    entidades.add(entidadA);
    entidades.add(entidadB);
    entidades.add(entidadC);

    List<Entidad> entidadesOrdenadas = mayorPromedioCierre.ordenar(entidades);

    Assertions.assertEquals(entidadB, entidadesOrdenadas.get(0));
    Assertions.assertEquals(entidadA, entidadesOrdenadas.get(1));
    Assertions.assertEquals(entidadC, entidadesOrdenadas.get(2));
  }

  private Entidad entidadConIncidentes(int cantidadIncidentes, String nombre) {
    Entidad unaEntidad = new Entidad(nombre, TipoDeEntidad.SUBTERRANEO);
    Establecimiento unEstablecimiento = new Establecimiento();
    Servicio unBanio = new Servicio("servicioInestable", TipoDeServicio.BANIOS);

    LocalDateTime fechaApertura = LocalDateTime.of(2023, 1, 1, 9, 0);

    for (int i = 0; i < cantidadIncidentes; i++) {
      Incidente incidente = new Incidente(unBanio, "No anda la cadena del banio - " + i);
      LocalDateTime fechaCierre = fechaApertura.plusHours(i * 2L); // a + incidentes + tardanza

      incidente.cerrar();
      incidente.setFecha(fechaApertura);
      incidente.setFechaResolucion(fechaCierre);
      unBanio.agregarIncidente(incidente);
    }
    unaEntidad.agregarEstablecimiento(unEstablecimiento);
    unEstablecimiento.agregarServicio(unBanio);

    return unaEntidad;
  }

  private OrganismoDeControl unOrganismoDeControlConTresIncidentes() {
    String nombre = "OrganismoDeControl";
    String correoElectronico = "hola@mail.com";
    OrganismoDeControl org = new OrganismoDeControl(nombre, correoElectronico);

    Entidad unaEntidad = entidadConIncidentes(1, "a");
    Entidad otraEntidad = entidadConIncidentes(2, "b");

    org.agregarEntidad(otraEntidad);
    org.agregarEntidad(unaEntidad);

    return org;
  }
}
