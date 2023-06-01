package ar.edu.utn.frba.dds.excepciones;

public class ValidacionContrasenaException extends RuntimeException {
  public ValidacionContrasenaException(String message) {
    super(message);
  }
}
