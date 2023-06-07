package ar.edu.utn.frba.dds.excepciones;

public class ArchivoCsvException extends RuntimeException {
  public ArchivoCsvException(String mensaje) {
    super(mensaje);
  }
}
