package ar.edu.utn.frba.dds.politicas;

import ar.edu.utn.frba.dds.validacion.PoliticaContrasena;
import ar.edu.utn.frba.dds.validacion.ValidacionContrasenaException;
import java.util.regex.Pattern;

public class PoliticaRegex implements PoliticaContrasena {
  private Pattern patron;
  private String mensajeError;

  public PoliticaRegex(String regex, String mensajeError) {
    this.patron = Pattern.compile(regex);
    this.mensajeError = mensajeError;
  }

  @Override
  public void validar(String contrasena) {
    if (!patron.matcher(contrasena).matches()) {
      throw new ValidacionContrasenaException(getMensajeError());
    }
  }

  @Override
  public String getMensajeError() {
    return mensajeError;
  }
}
