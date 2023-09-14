package db;


import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContextTest implements SimplePersistenceTest {

  @Test
  void crearEntidad() {
    var entidadDeSubtes = new Entidad("entidad", TipoDeEntidad.SUBTERRANEO);

    entityManager().persist(entidadDeSubtes);

    var id = entidadDeSubtes.getId();

    entityManager().flush();
    entityManager().clear();

    var entidad = entityManager().find(Entidad.class, id);

    assertNotNull(entidad);
  }
}
