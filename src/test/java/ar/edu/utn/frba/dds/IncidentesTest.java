package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IncidentesTest {

  @Test
  public void puedoAbrirUnIncidenteEnUnaComunidad() {
    Comunidad nosMovemosEnSubte = new Comunidad();
    Servicio ascensor = new Servicio("Ascensor - acceso a estación", TipoDeServicio.ASCENSORES);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio");
    assertEquals(1, nosMovemosEnSubte.getIncidentes().size());
  }

  @Test
  public void abrirIncidenteDeServicioNoInteresLanzaExcepcion() {
    Comunidad nosMovemosEnSubte = new Comunidad();
    Servicio ascensor = new Servicio("Ascensor - acceso a estación", TipoDeServicio.ASCENSORES);
    assertThrows(RuntimeException.class, () ->
        nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio"));
  }

  @Test
  public void puedoCerrarUnIncidenteEnUnaComunidad() {
    Comunidad nosMovemosEnSubte = new Comunidad();
    Servicio ascensor = new Servicio("Ascensor - acceso a estación", TipoDeServicio.ASCENSORES);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    Incidente incidente = nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio");
    incidente.cerrar();
    assertTrue(incidente.estaResuelto());
    assertNotNull(incidente.getFechaResolucion());
  }
}
