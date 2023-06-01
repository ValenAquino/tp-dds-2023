package ar.edu.utn.frba.dds.excepciones;

public class LocalizacionNoExistenteException extends RuntimeException {
  public LocalizacionNoExistenteException(String message) {
    super(message);
  }
}
