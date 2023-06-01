package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.localizacion.Localizacion;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Entidad {
  private String nombre;
  private Localizacion localizacion;
  private List<Establecimiento> establecimientos;
  public Entidad(String nombre, Localizacion localizacion){
    this.localizacion = localizacion;
    this.nombre=nombre;
  }
}
