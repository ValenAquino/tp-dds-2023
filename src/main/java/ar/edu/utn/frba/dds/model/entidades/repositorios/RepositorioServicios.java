package ar.edu.utn.frba.dds.model.entidades.repositorios;

import ar.edu.utn.frba.dds.model.entidades.Comunidad;
import ar.edu.utn.frba.dds.model.entidades.Entidad;
import ar.edu.utn.frba.dds.model.entidades.Establecimiento;
import ar.edu.utn.frba.dds.model.entidades.Incidente;
import ar.edu.utn.frba.dds.model.entidades.Servicio;
import ar.edu.utn.frba.dds.model.entidades.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class RepositorioServicios implements WithSimplePersistenceUnit {
  private static RepositorioServicios instance;

  public static RepositorioServicios getInstance() {
    if (instance == null) {
      instance = new RepositorioServicios();
    }
    return instance;
  }

  public void persistir(Servicio servicio) {
    entityManager().persist(servicio);
  }
  public List<Servicio> todos() {
    return entityManager().createQuery("from Servicio", Servicio.class)
        .getResultList();
  }
  public Servicio porId(Integer id) {
    return entityManager().find(Servicio.class, id);
  }
  public List<Servicio> porUsuario(Usuario usuario) {
    return entityManager()
        .createQuery("SELECT distinct s FROM Comunidad c" +
            " JOIN c.serviciosDeInteres s" +
            " WHERE :usuario MEMBER OF c.miembros", Servicio.class)
        .setParameter("usuario", usuario)
        .getResultList();
  }
}
