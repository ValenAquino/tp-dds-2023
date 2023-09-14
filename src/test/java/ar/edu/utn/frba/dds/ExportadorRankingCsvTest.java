package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.enums.TipoDeEntidad;
import ar.edu.utn.frba.dds.entidades.rankings.Ranking;
import ar.edu.utn.frba.dds.exportadores.ExportadorRankingCsv;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExportadorRankingCsvTest {
  private static final String DIRECTORIO_TEMPORAL = "reportes";
  private static Path PathDirectorioTemporal;
  final Ranking ranking = mock(Ranking.class);
  final Entidad subteA = new Entidad("subteA", TipoDeEntidad.SUBTERRANEO);
  final Entidad lineaSarmiento = new Entidad("lineaSarmiento", TipoDeEntidad.FERROCARRIL);

  @BeforeAll
  static void setUp() throws IOException {
    // Configurar el directorio temporal antes de que se ejecuten los tests
    PathDirectorioTemporal = Paths.get(DIRECTORIO_TEMPORAL);
    Files.createDirectories(PathDirectorioTemporal);
  }

  @AfterAll
  static void tearDown() throws IOException {
    Files.walk(PathDirectorioTemporal)
        .map(Path::toFile)
        .forEach(File::delete);

    Files.delete(PathDirectorioTemporal);
  }

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
    return Paths.get(DIRECTORIO_TEMPORAL, nombreArchivo).toAbsolutePath();
  }
}
