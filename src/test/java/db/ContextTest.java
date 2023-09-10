package db;


import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.notificaciones.medios.MailSender;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class ContextTest implements SimplePersistenceTest {

  @Test
  void contextUp() {
    assertNotNull(entityManager());
  }
  @Test
  void contextUpWithTransaction() throws Exception {
    withTransaction(() -> {
    });
  }
}
