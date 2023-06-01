package ar.edu.utn.frba.dds.password.politicas;

import ar.edu.utn.frba.dds.excepciones.ValidacionContrasenaException;
import ar.edu.utn.frba.dds.password.validacion.PoliticaContrasena;
import java.util.HashSet;
import java.util.Set;

public class PoliticaContrasenasExcluidas implements PoliticaContrasena {

  private final Set<String> contrasenasExcluidas;

  public PoliticaContrasenasExcluidas(Set<String> peoresContrasenas) {
    this.contrasenasExcluidas = new HashSet<>(peoresContrasenas);
  }

  @Override
  public void validar(String contrasena) {
    if (contrasenasExcluidas.contains(contrasena)) {
      throw new ValidacionContrasenaException(getMensajeError());
    }
  }

  @Override
  public String getMensajeError() {
    return
        "La contraseña ingresada se encuentra en la lista "
            + "de contraseñas excluídas. Por favor, ingresá otra.";
  }
}
