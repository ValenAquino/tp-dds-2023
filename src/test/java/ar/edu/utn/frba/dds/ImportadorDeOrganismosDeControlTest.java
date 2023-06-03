package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.importadores.OrganismoDeControlImportador;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Paths;
import java.util.List;

public class OrganismoDeControlImportadorTest {

  private List<OrganismoDeControl> organismosDeControl;

  @BeforeEach
  public void inicializarImportador() throws URISyntaxException {
    var url = OrganismoDeControlImportadorTest.class
        .getClassLoader()
        .getResource("20organismos.csv");

    assertNotNull(url);

    OrganismoDeControlImportador importador = new OrganismoDeControlImportador(
        Paths.get(url.toURI()).toString());

    organismosDeControl = importador.getOrganismosDeControl();
  }

  @Test
  public void puedoObtener20OrganismosDeControl() {
    assertNotNull(organismosDeControl);
    assertEquals(20, organismosDeControl.size());
  }

  @Test
  public void seCarganBienLosNombres() {
    assertEquals("Organismo12", organismosDeControl.get(11).getNombre());
  }
}
