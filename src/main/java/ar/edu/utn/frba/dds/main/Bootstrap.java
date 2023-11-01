package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioNotificaciones;
import ar.edu.utn.frba.dds.model.entidades.repositorios.RepositorioUsuarios;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class Bootstrap implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    new Bootstrap().run();
  }

  public void run() {
    withTransaction(() -> {
      var usuario = new Usuario(
          "alumnofrba",
          "12345",
          "Alumno",
          "Modelo",
          "alumno@frba.utn.edu.ar",
          RepositorioComunidades.getInstance(),
          RepositorioNotificaciones.getInstance()
      );

      RepositorioUsuarios.getInstance().persistir(usuario);
    });
  }
}
