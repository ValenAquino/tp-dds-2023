package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.entidades.rankings.GeneradorRankingSemanal;
import ar.edu.utn.frba.dds.entidades.rankings.criterios.CantidadIncidentes;
import org.junit.jupiter.api.Test;

public class GeneradorRankingSemanalTest {

  @Test
  public void test() {
    OrganismoDeControl organismo = unOrganismoDeControl();
    CantidadIncidentes cantidadIncidentes = new CantidadIncidentes("CantidadIncidentes");
    GeneradorRankingSemanal generadorRankingSemanal = new GeneradorRankingSemanal(organismo);
    generadorRankingSemanal.agregarCriterio(cantidadIncidentes);

    generadorRankingSemanal.generarRankingSemanal();
  }

  private OrganismoDeControl unOrganismoDeControl() {
    String nombre = "OrganismoDeControl";
    String correoElectronico = "hola@mail.com";
    return new OrganismoDeControl(nombre, correoElectronico);
  }
}
