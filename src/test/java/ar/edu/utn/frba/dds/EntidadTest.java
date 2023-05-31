package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.localizacion.Localizacion;
import org.junit.jupiter.api.Test;
import java.util.List;

public class EntidadTest {
  @Test
  public void puedoObtenerListaDeEntidades(){
    String basePath = System.getProperty("user.dir");
    String csvFile = "docs/Entidades.csv";
    Localizacion localizacion = new Localizacion("Caballito");
    Entidad entidad = new Entidad("Entidad",localizacion);
    List<Entidad> entidades = entidad.getEntidadesDeCSV(basePath + "/" + csvFile);
    assertNotNull(entidades);
  }
}
