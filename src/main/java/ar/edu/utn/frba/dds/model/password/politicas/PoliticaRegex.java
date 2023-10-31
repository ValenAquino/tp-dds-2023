package ar.edu.utn.frba.dds.model.password.politicas;

import ar.edu.utn.frba.dds.model.password.validacion.PoliticaContrasena;
import java.util.regex.Pattern;

public class PoliticaRegex implements PoliticaContrasena {
  private final Pattern patron;
  private final String mensajeError;

  public PoliticaRegex(String regex, String mensajeError) {
    this.patron = Pattern.compile(regex);
    this.mensajeError = mensajeError;
  }

  @Override
  public boolean esValida(String contrasena) {
    return patron.matcher(contrasena).matches();
  }

  @Override
  public String getMensajeError() {
    return mensajeError;
  }
}
