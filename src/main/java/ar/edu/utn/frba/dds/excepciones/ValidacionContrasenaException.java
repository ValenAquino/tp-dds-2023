package ar.edu.utn.frba.dds.excepciones;

import ar.edu.utn.frba.dds.password.validacion.PoliticaContrasena;
import java.util.List;

public class ValidacionContrasenaException extends RuntimeException {
  public List<PoliticaContrasena> politicasAValidar;
  public ValidacionContrasenaException(List<PoliticaContrasena> politicasAValidar, String message) {
    super(message);
    this.politicasAValidar = politicasAValidar;

  }
  public List<PoliticaContrasena> getPoliticasAValidar() {
    return politicasAValidar;
  }
}
