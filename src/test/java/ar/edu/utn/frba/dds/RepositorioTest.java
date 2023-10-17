package ar.edu.utn.frba.dds;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.entidades.Comunidad;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeServicio;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioNotificaciones;
import ar.edu.utn.frba.dds.notificaciones.MedioDeComunicacion;
import ar.edu.utn.frba.dds.notificaciones.medios.MailSender;
import ar.edu.utn.frba.dds.notificaciones.medios.WhatsAppSender;
import ar.edu.utn.frba.dds.ubicacion.ServicioMapas;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class RepositorioTest {

  private Usuario usuarioQueUsaSubte;
  private Usuario usuariaQueUsaSubte;

  private RepositorioComunidades repositorioComunidades;
  private RepositorioNotificaciones repositorioNotificaciones;

  private MedioDeComunicacion medioDeComunicacion;
  private Comunidad nosMovemosEnSubte;
  private ServicioMapas servicioMapas;

  private final Servicio ascensor = new Servicio(
      "Ascensor - acceso a estación",
      TipoDeServicio.ASCENSORES
  );
  private final Servicio escaleraMecanica = new Servicio(
      "Escalera mecánica - acceso a andén",
      TipoDeServicio.ESCALERAS_MECANICAS
  );


  @BeforeEach
  public void startUp(){
    repositorioComunidades = new RepositorioComunidades();
    repositorioNotificaciones = new RepositorioNotificaciones();
    usuarioQueUsaSubte = new Usuario(
        "subteMaster13",
        "",
        "Subte",
        "Master",
        "subtemaster@gmail.com",
        repositorioComunidades,
        repositorioNotificaciones
    );
    usuariaQueUsaSubte = new Usuario(
        "subteLover43",
        "",
        "Subte",
        "Lover",
        "subtelover@gmail.com",
        repositorioComunidades,
        repositorioNotificaciones
    );
    medioDeComunicacion = new WhatsAppSender();
    usuariaQueUsaSubte.setMedioDeComunicacion(medioDeComunicacion);
    usuarioQueUsaSubte.setMedioDeComunicacion(medioDeComunicacion);
    nosMovemosEnSubte = new Comunidad(servicioMapas);
    nosMovemosEnSubte.agregarMiembro(usuarioQueUsaSubte);
    nosMovemosEnSubte.agregarMiembro(usuariaQueUsaSubte);
    nosMovemosEnSubte.agregarServicioDeInteres(ascensor);
    nosMovemosEnSubte.agregarServicioDeInteres(escaleraMecanica);
    repositorioComunidades.persistir(nosMovemosEnSubte);


  }

  @Test
  public void sePuedenPersistirComunidades() {
    List<Comunidad> comunidadesObtenidas = repositorioComunidades.todas();
    Assertions.assertTrue(comunidadesObtenidas.contains(nosMovemosEnSubte));
  }
  @Test
  public void sePuedenPersistirLosUsuariosDeUnaComunidad() {
    List<Comunidad> comunidadesObtenidas = repositorioComunidades.todas();
    Assertions.assertTrue(comunidadesObtenidas.get(0).tieneMiembro(usuarioQueUsaSubte));
    Assertions.assertTrue(comunidadesObtenidas.get(0).tieneMiembro(usuariaQueUsaSubte));
  }
  @Test
  public void sePuedenPersistirLosIncidentesDeUnaComunidad() {
    usuariaQueUsaSubte.reportarIncidente(ascensor,"Fuera de servicio");
    repositorioComunidades.persistir(nosMovemosEnSubte);

    List<Comunidad> comunidadesObtenidas = repositorioComunidades.todas();
    Assertions.assertEquals(comunidadesObtenidas.get(0).getIncidentes().size(),1);
  }
}
