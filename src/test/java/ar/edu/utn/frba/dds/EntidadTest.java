package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.Establecimiento;
import ar.edu.utn.frba.dds.entidades.Incidente;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.TipoDeServicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntidadTest {
  @Test
  public void unaEntidadConoceLosIncidentesDeSusEstablecimientos() {
    Entidad unaEntidad = new Entidad("entidad", TipoDeEntidad.SUBTERRANEO);

    Establecimiento unaEstacion = new Establecimiento();
    Establecimiento otraEstacion = new Establecimiento();

    Servicio unBanio = new Servicio("servicioInestable", TipoDeServicio.BANIOS);
    Servicio unAscensor = new Servicio("ascensorInestable", TipoDeServicio.ASCENSORES);

    Incidente incidente = new Incidente(unBanio, "No anda la cadena");
    Incidente otroIncidente = new Incidente(unAscensor, "No anda el ascensor");

    unaEntidad.agregarEstablecimiento(unaEstacion);
    unaEntidad.agregarEstablecimiento(otraEstacion);

    unaEstacion.agregarServicio(unBanio);
    otraEstacion.agregarServicio(unAscensor);

    unBanio.agregarIncidente(incidente);
    unAscensor.agregarIncidente(otroIncidente);

    Assertions.assertEquals(2, unaEntidad.getIncidentes().size());
  }
}
