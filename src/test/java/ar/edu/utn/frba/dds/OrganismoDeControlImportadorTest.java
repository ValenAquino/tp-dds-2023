package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.importadores.OrganismoDeControlImportador;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class OrganismoDeControlImportadorTest {

  private List<OrganismoDeControl> organismosDeControl;
  @BeforeEach
  public void inicializarImportador() {
    URL resourceUrl = getClass().getClassLoader().getResource("20organismos.csv");
    assert resourceUrl != null;
    OrganismoDeControlImportador importador = new OrganismoDeControlImportador(resourceUrl.getPath());
    organismosDeControl = importador.getOrganismosDeControl();
  }

  @Test
  public void puedoObtener20OrganismosDeControl(){
    assertNotNull(organismosDeControl);
    assertEquals(20, organismosDeControl.size());
  }

  @Test
  public void seCarganBienLosNombres(){
    assertEquals("Organismo12", organismosDeControl.get(11).getNombre());
  }
}
