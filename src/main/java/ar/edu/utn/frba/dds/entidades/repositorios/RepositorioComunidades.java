package ar.edu.utn.frba.dds.entidades.repositorios;

import ar.edu.utn.frba.dds.entidades.Comunidad;
import ar.edu.utn.frba.dds.entidades.Servicio;
import ar.edu.utn.frba.dds.entidades.Usuario;
import java.util.ArrayList;
import java.util.List;

public class RepositorioComunidades {
  private static RepositorioComunidades instance;
  private final List<Comunidad> comunidades = new ArrayList<>();

  public static RepositorioComunidades getInstance() {
    if (instance == null) {
      instance = new RepositorioComunidades();
    }
    return instance;
  }

  public List<Comunidad> todas() {
    return this.comunidades;
  }

  public void agregarComunidad(Comunidad comunidad) {
    comunidades.add(comunidad);
  }

  public List<Comunidad> getComunidadesDe(Usuario usuario) {
    return todas().stream()
        .filter(c -> c.tieneMiembro(usuario))
        .toList();
  }

  public List<Comunidad> getComunidadesInteresadas(Usuario usuario, Servicio servicio) {
    return getComunidadesDe(usuario).stream()
        .filter(c -> c.leInteresaServicio(servicio))
        .toList();
  }
}
