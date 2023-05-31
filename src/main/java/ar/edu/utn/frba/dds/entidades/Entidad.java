package ar.edu.utn.frba.dds.entidades;

import ar.edu.utn.frba.dds.localizacion.Localizacion;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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
  public List<Entidad> getEntidadesDeCSV(String path){
    List<Entidad> entidades = new ArrayList<>();

    try (Reader reader = new FileReader(path);
      CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("nombre", "nombreLocalizacion", "latitud", "longitud"))) {
      csvParser.forEach(csvRecord -> {
        String nombre = csvRecord.get("nombre");
        String nombreLocalizacion = csvRecord.get("nombreLocalizacion");
        String latitud = csvRecord.get("latitud");
        String longitud = csvRecord.get("longitud");
        entidades.add(new Entidad(nombre,new Localizacion(nombreLocalizacion)));
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return entidades;
  }
  public String toString(){
    return this.nombre + " - " + this.localizacion.getNombre();
  }
}
