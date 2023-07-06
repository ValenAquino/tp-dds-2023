package ar.edu.utn.frba.dds.entidades;

import java.util.List;

public class RepositorioUsuarios {
  private static RepositorioUsuarios instance;
  private List<Usuario> usuarios;
  public List<Usuario> todos(){
    return this.usuarios;
  }
  public static RepositorioUsuarios getInstance(){
    if(instance==null){
      instance = new RepositorioUsuarios();
    }
    return instance;
  }
}
