package ar.edu.utn.frba.dds.password.politicas;

import ar.edu.utn.frba.dds.password.validacion.PoliticaContrasena;

public class PoliticaLongitud implements PoliticaContrasena {
  private final int longitudMinima;
  private final int longitudMaxima;

  public PoliticaLongitud(int longitudMinima, int longitudMaxima) {
    this.longitudMinima = longitudMinima;
    this.longitudMaxima = longitudMaxima;
  }

  @Override
  public boolean esValida(String contrasena) {
    return contrasena.length() >= longitudMinima && contrasena.length() <= longitudMaxima;
  }

  @Override
  public String getMensajeError() {
    return String.format(
        "La contraseña debe tener entre %d y %d caracteres.", longitudMinima, longitudMaxima
    );
  }
}