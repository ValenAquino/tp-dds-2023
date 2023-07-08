package ar.edu.utn.frba.dds.excepciones;

public class GeneradorCsvException extends RuntimeException {
  public GeneradorCsvException(String mensaje) {
    super(mensaje);
  }
}
