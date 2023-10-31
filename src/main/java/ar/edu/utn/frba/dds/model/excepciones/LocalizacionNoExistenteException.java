package ar.edu.utn.frba.dds.model.excepciones;

public class LocalizacionNoExistenteException extends RuntimeException {
  public LocalizacionNoExistenteException(String message) {
    super(message);
  }
}
