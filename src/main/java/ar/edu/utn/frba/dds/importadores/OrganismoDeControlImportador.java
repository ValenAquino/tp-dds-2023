package ar.edu.utn.frba.dds.importadores;

import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class OrganismoDeControlImportador {

  private final String pathCsv;

  public OrganismoDeControlImportador(String path)  {
    this.pathCsv = path;
  }

  public List<OrganismoDeControl> getOrganismosDeControl() {
    List<OrganismoDeControl> organismos = new ArrayList<>();

    try (Reader reader = new FileReader(pathCsv);
         CSVParser csvParser = CSVParser.parse(
             reader,
             CSVFormat.DEFAULT.withHeader("nombre", "correo"))
    ) {
      csvParser.forEach(csvRecord -> {
        String nombre = csvRecord.get("nombre");
        String correo = csvRecord.get("correo");
        organismos.add(new OrganismoDeControl(nombre, correo));
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return organismos;
  }
}
