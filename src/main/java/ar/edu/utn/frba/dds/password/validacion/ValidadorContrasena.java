package ar.edu.utn.frba.dds.password.validacion;

import ar.edu.utn.frba.dds.excepciones.ValidacionContrasenaException;
import java.util.ArrayList;
import java.util.List;

public class ValidadorContrasena {
  private final List<PoliticaContrasena> politicas;

  public ValidadorContrasena(List<PoliticaContrasena> politicas) {
    this.politicas = new ArrayList<>(politicas);
  }

  public void validar(String contrasena) {
    for (PoliticaContrasena politica : politicas) {
      if (!politica.esValida(contrasena)) {
        throw new ValidacionContrasenaException(politica.getMensajeError());
      }
    }
  }
}

