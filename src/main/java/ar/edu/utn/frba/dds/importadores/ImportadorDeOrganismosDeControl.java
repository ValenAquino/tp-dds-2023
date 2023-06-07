package ar.edu.utn.frba.dds.importadores;

import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.excepciones.ArchivoCSVException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVRecord;

public class ImportadorDeOrganismosDeControl {
  ArchivoParseableCSV archivoParseableCSV;
  List<OrganismoDeControl> organismos = new ArrayList<>();

  public ImportadorDeOrganismosDeControl(ArchivoParseableCSV archivoParseableCSV) {
    this.archivoParseableCSV = archivoParseableCSV;
  }

  public List<OrganismoDeControl> getOrganismosDeControl() {
    if (organismos.isEmpty()) {
      parsear();
    }

    return organismos;
  }

  public void parsear() {
    try {
      List<CSVRecord> csvRecords = archivoParseableCSV.getRecordsValidas();

      CSVRecord headerRecord = csvRecords.remove(0);
      int nombreIndex = headerRecord.toList().indexOf("nombre");
      int correoIndex = headerRecord.toList().indexOf("correo");

      for (CSVRecord csvRecord : csvRecords) {
        String nombre = csvRecord.get(nombreIndex);
        String correo = csvRecord.get(correoIndex);

        organismos.add(organismoDeControlValido(nombre, correo));
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public OrganismoDeControl organismoDeControlValido(String nombre, String correo) {
    nombreValido(nombre);
    correoValido(correo);
    return new OrganismoDeControl(nombre, correo);
  }

  public void nombreValido(String nombre) {
    if (nombre == null || nombre.isEmpty()) {
      throw new ArchivoCSVException("El nombre no puede ser vacio");
    }

    if (nombre.contains("@")) {
      throw new ArchivoCSVException("El archivo contiene un nombre invalido");
    }
  }

  public void correoValido(String correo) {
    if (correo.isEmpty()) {
      throw new ArchivoCSVException("El correo no puede ser vacio");
    }

    if (!correo.contains("@")) {
      throw new ArchivoCSVException("El archivo contiene un correo invalido");
    }
  }

}
