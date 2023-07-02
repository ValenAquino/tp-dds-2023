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

  @Test
  public void puedoConsultarIncidentesPorEstado() {
    Comunidad nosMovemosEnSubte = new Comunidad();
    Servicio ascensor = new Servicio("Ascensor - acceso a estación", TipoDeServicio.ASCENSORES);
    Servicio escaleraMecanica = new Servicio("Escalera mecánica - acceso a andén", TipoDeServicio.ESCALERAS_MECANICAS);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.agregarServicioDeInteres(escaleraMecanica);

    Incidente incidenteAscensor = nosMovemosEnSubte.abrirIncidente(ascensor, "Fuera de servicio");
    Incidente incidenteEscalera = nosMovemosEnSubte.abrirIncidente(escaleraMecanica, "Fuera de servicio");
    incidenteAscensor.cerrar();

    assertEquals(1, nosMovemosEnSubte.getIncidentesAbiertos().size());
    assertEquals(1, nosMovemosEnSubte.getIncidentesResueltos().size());
    assertEquals(incidenteAscensor, nosMovemosEnSubte.getIncidentesResueltos().get(0));
    assertEquals(incidenteEscalera, nosMovemosEnSubte.getIncidentesAbiertos().get(0));
  }
}
