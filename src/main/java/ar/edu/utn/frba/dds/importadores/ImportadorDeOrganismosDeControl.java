package ar.edu.utn.frba.dds.importadores;

import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import ar.edu.utn.frba.dds.excepciones.ArchivoCsvException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVRecord;

public class ImportadorDeOrganismosDeControl {
  ArchivoParseableCsv archivoParseableCsv;
  List<OrganismoDeControl> organismos = new ArrayList<>();

  public ImportadorDeOrganismosDeControl(ArchivoParseableCsv archivoParseableCsv) {
    this.archivoParseableCsv = archivoParseableCsv;
  }

  public List<OrganismoDeControl> getOrganismosDeControl() {
    if (organismos.isEmpty()) {
      parsear();
    }

    return organismos;
  }

  public void parsear() {
    try {
      List<CSVRecord> csvRecords = archivoParseableCsv.getRecordsValidas();

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
      throw new ArchivoCsvException("El nombre no puede ser vacio");
    }

    if (nombre.contains("@")) {
      throw new ArchivoCsvException("El archivo contiene un nombre invalido");
    }
  }

  public void correoValido(String correo) {
    if (correo.isEmpty()) {
      throw new ArchivoCsvException("El correo no puede ser vacio");
    }

    if (!correo.contains("@")) {
      throw new ArchivoCsvException("El archivo contiene un correo invalido");
    }
  }

}
