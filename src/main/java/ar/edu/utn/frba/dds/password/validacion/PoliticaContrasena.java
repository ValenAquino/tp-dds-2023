package ar.edu.utn.frba.dds.password.validacion;

public interface PoliticaContrasena {
  boolean esValida(String contrasena);

  String getMensajeError();
}