package ar.edu.utn.frba.dds.model.excepciones;

public class GeneradorCsvException extends RuntimeException {
  public GeneradorCsvException(String mensaje) {
    super(mensaje);
  }
}
