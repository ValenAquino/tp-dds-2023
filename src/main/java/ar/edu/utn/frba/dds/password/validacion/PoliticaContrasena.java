package ar.edu.utn.frba.dds.password.validacion;

public interface PoliticaContrasena {
  void validar(String contrasena);

  String getMensajeError();
}