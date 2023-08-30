package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.exportadores.ExportadorRankingCsv;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExportadorRankingCsvTest {
  Ranking ranking = mock(Ranking.class);
  Entidad subteA = new Entidad("subteA", TipoDeEntidad.SUBTERRANEO);
  Entidad lineaSarmiento = new Entidad("lineaSarmiento", TipoDeEntidad.FERROCARRIL);

  /*
  * afterEach cleanUp() borrar directorio reportes ??
  * */

  @Test
  void seExportaUnRankingAcsv() {
    ExportadorRankingCsv exportador = new ExportadorRankingCsv(ranking);
    LocalDateTime fecha = LocalDateTime.now();

    when(ranking.getDescripcionCriterio()).thenReturn("Mayor cantidad de incidentes");
    when(ranking.getFecha()).thenReturn(fecha);
    when(ranking.getEntidades()).thenReturn(this.entidadesDePrueba());

    exportador.exportar();

    String nombreArchivoEsperado = getNombreArchivo(
        fecha, ranking.getDescripcionCriterio()
    );

    assertTrue(
        Files.exists(obtenerPathAbsoluto(nombreArchivoEsperado)),
        "Debería haberse creado el archivo CSV"
    );
  }

  public Map<Entidad, Double> entidadesDePrueba() {
    Map<Entidad, Double> entidades = new HashMap<>();
    entidades.put(subteA, 2.0);
    entidades.put(lineaSarmiento, 2.0);
    return entidades;
  }

  public String getNombreArchivo(LocalDateTime fecha, String criterioConEspacios) {
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    String fechaFormateada = fecha.format(formato);
    String criterio = criterioConEspacios.replace(" ", "_");

    return String.format(
        "Ranking_%s_%s.csv", criterio, fechaFormateada
    );
  }

  public Path obtenerPathAbsoluto(String nombreArchivo) {
    return Paths.get("reportes", nombreArchivo).toAbsolutePath();
  }
}
