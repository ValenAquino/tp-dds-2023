package ar.edu.utn.frba.dds.model.password.validacion;

public interface PoliticaContrasena {
  boolean esValida(String contrasena);

  String getMensajeError();
}