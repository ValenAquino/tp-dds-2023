package ar.edu.utn.frba.dds.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PersistentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int id;

  public int getId(){
    return id;
  }
}
