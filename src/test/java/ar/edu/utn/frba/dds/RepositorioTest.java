package ar.edu.utn.frba.dds;

import static org.mockito.Mockito.mock;

import ar.edu.utn.frba.dds.entidades.Comunidad;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Usuario;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.entidades.repositorios.RepositorioNotificaciones;
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
  private Comunidad usuariosDeSubte;
  private ServicioMapas servicioMapas;


  @BeforeEach
  public void startUp(){
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
    repositorioComunidades = new RepositorioComunidades();
    repositorioNotificaciones = new RepositorioNotificaciones();
    usuariosDeSubte = new Comunidad(servicioMapas);
    usuariosDeSubte.agregarMiembro(usuarioQueUsaSubte);
    usuariosDeSubte.agregarMiembro(usuariaQueUsaSubte);
  }

  @Test
  public void sePuedenPersistirComunidades() {
    repositorioComunidades.persistir(usuariosDeSubte);
    List<Comunidad> comunidadesObtenidas = repositorioComunidades.todas();

    Assertions.assertTrue(comunidadesObtenidas.contains(usuariosDeSubte));
  }
  @Test
  public void sePuedenPersistirLosUsuariosDeUnaComunidad() {
    repositorioComunidades.persistir(usuariosDeSubte);
    List<Comunidad> comunidadesObtenidas = repositorioComunidades.todas();

    Assertions.assertTrue(comunidadesObtenidas.get(0).tieneMiembro(usuarioQueUsaSubte));
    Assertions.assertTrue(comunidadesObtenidas.get(0).tieneMiembro(usuariaQueUsaSubte));
  }
 
}
