package ar.edu.utn.frba.dds.model.password.validacion;

import ar.edu.utn.frba.dds.model.excepciones.ValidacionContrasenaException;
import java.util.ArrayList;
import java.util.List;

public class ValidadorContrasena {
  private final List<PoliticaContrasena> politicas;

  public ValidadorContrasena(List<PoliticaContrasena> politicas) {
    this.politicas = new ArrayList<>(politicas);
  }

  public void validar(String contrasena) {
    var politicasAValidar = politicas.stream().filter(p -> !p.esValida(contrasena)).toList();

    if (politicasAValidar.size() > 0) {
      throw new ValidacionContrasenaException(politicasAValidar,
          getMensajeError(politicasAValidar));
    }
  }

  private String getMensajeError(List<PoliticaContrasena> politicasAValidar) {
    StringBuilder stringBuilder = new StringBuilder();

    for (PoliticaContrasena politica : politicasAValidar) {
      stringBuilder.append(politica.getMensajeError()).append(System.lineSeparator());
    }

    return stringBuilder.toString();
  }
}

