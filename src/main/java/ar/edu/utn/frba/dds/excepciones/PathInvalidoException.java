package ar.edu.utn.frba.dds.excepciones;

public class PathInvalidoException extends RuntimeException {
    public PathInvalidoException(String mensaje) {
      super(mensaje);
    }
}
