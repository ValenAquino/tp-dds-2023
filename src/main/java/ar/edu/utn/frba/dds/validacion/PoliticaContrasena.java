package ar.edu.utn.frba.dds.validacion;

public interface PoliticaContrasena {
  void validar(String contrasena);

  String getMensajeError();
}