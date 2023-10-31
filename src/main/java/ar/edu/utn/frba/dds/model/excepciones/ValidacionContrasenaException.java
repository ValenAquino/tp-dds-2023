package ar.edu.utn.frba.dds.model.excepciones;

import ar.edu.utn.frba.dds.model.password.validacion.PoliticaContrasena;
import java.util.List;

public class ValidacionContrasenaException extends RuntimeException {
  public final List<PoliticaContrasena> politicasAValidar;

  public ValidacionContrasenaException(List<PoliticaContrasena> politicasAValidar, String message) {
    super(message);
    this.politicasAValidar = politicasAValidar;

  }

  public List<PoliticaContrasena> getPoliticasAValidar() {
    return politicasAValidar;
  }
}
