package ar.edu.utn.frba.dds.politicas;

import ar.edu.utn.frba.dds.validacion.PoliticaContrasena;
import ar.edu.utn.frba.dds.validacion.ValidacionContrasenaException;

public class PoliticaLongitud implements PoliticaContrasena {
  private final int longitudMinima;
  private final int longitudMaxima;

  public PoliticaLongitud(int longitudMinima, int longitudMaxima) {
    this.longitudMinima = longitudMinima;
    this.longitudMaxima = longitudMaxima;
  }

  @Override
  public void validar(String contrasena) {
    if (contrasena.length() < longitudMinima || contrasena.length() > longitudMaxima) {
      throw new ValidacionContrasenaException(getMensajeError());
    }
  }

  @Override
  public String getMensajeError() {
    return String.format("La contraseña debe tener entre %d y %d caracteres.", longitudMinima, longitudMaxima);
  }
}