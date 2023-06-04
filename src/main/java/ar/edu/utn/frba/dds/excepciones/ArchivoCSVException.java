package ar.edu.utn.frba.dds.excepciones;

public class ArchivoCSVException extends RuntimeException {
    public ArchivoCSVException(String mensaje) {
      super(mensaje);
    }
}
