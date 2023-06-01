package ar.edu.utn.frba.dds.localizacion;

public class LocalizacionNoExistenteException extends RuntimeException {
  public LocalizacionNoExistenteException(String message) {
    super(message);
  }
}
