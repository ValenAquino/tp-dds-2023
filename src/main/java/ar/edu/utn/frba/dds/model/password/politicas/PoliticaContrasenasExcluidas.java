package ar.edu.utn.frba.dds.model.password.politicas;

import ar.edu.utn.frba.dds.model.password.validacion.PoliticaContrasena;
import java.util.HashSet;
import java.util.Set;

public class PoliticaContrasenasExcluidas implements PoliticaContrasena {

  private final Set<String> contrasenasExcluidas;

  public PoliticaContrasenasExcluidas(Set<String> peoresContrasenas) {
    this.contrasenasExcluidas = new HashSet<>(peoresContrasenas);
  }

  @Override
  public boolean esValida(String contrasena) {
    return !contrasenasExcluidas.contains(contrasena);
  }

  @Override
  public String getMensajeError() {
    return
        "La contraseña ingresada se encuentra en la lista "
            + "de contraseñas excluidas. Por favor, ingresá otra.";
  }
}
