package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.password.politicas.PoliticaContrasenasExcluidas;
import ar.edu.utn.frba.dds.password.politicas.PoliticaLongitud;
import ar.edu.utn.frba.dds.password.politicas.PoliticaRegex;
import ar.edu.utn.frba.dds.password.validacion.PoliticaContrasena;
import ar.edu.utn.frba.dds.excepciones.ValidacionContrasenaException;
import ar.edu.utn.frba.dds.password.validacion.ValidadorContrasena;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PoliticaContrasenaTest {

  private static final String excluidasMensajeError =
      "La contraseña ingresada se encuentra en la lista de contraseñas excluidas. Por favor, ingresá otra.";
  private static final String longitudMensajeError =
      "La contraseña debe tener entre 8 y 16 caracteres.";
  private static final String regexMensajeError =
      "La contraseña debe tener al menos una letra minúscula, una mayúscula, un número y un caracter especial.";

  @Test
  public void testPoliticaLongitudValida() {
    PoliticaLongitud politica = new PoliticaLongitud(8, 16);

    assertTrue(politica.esValida("contraseña"));
  }

  @Test
  public void testPoliticaLongitudInvalida() {
    PoliticaLongitud politica = new PoliticaLongitud(8, 16);

    assertFalse(politica.esValida("corta"));
  }

  @Test
  public void testPoliticaRegexValida() {
    PoliticaRegex politica = new PoliticaRegex(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
        regexMensajeError
    );

    assertTrue(politica.esValida("Contraseña1!"));
  }

  @Test
  public void testPoliticaRegexInvalida() {
    PoliticaRegex politica = new PoliticaRegex(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
        regexMensajeError
    );

    assertFalse(politica.esValida("contraseña"));
  }

  @Test
  public void testPoliticaContrasenasExcluidasValida() {
    PoliticaContrasenasExcluidas politica = new PoliticaContrasenasExcluidas(
        new HashSet<>(Collections.singletonList("contraseña")));

    assertTrue(politica.esValida("otraContraseña"));
  }

  @Test
  public void testPoliticaContrasenasExcluidasInvalida() {
    PoliticaContrasenasExcluidas politica = new PoliticaContrasenasExcluidas(
        new HashSet<>(Collections.singletonList("contraseña")));

    assertFalse(politica.esValida("contraseña"));
  }

  @Test
  public void testPoliticaContrasenasExcluidasArchivoValida() throws IOException {
    PoliticaContrasenasExcluidas politica = new PoliticaContrasenasExcluidas(
        cargarPeoresContrasenas());

    assertTrue(politica.esValida("contraseñaNoExcluida"));
  }

  @Test
  public void testPoliticaContrasenasExcluidasArchivoInvalida() throws IOException {
    PoliticaContrasenasExcluidas politica = new PoliticaContrasenasExcluidas(
        cargarPeoresContrasenas());

    assertFalse(politica.esValida("superman"));
  }

  @Test
  public void testValidadorContrasenaLongitudInvalida() {
    List<PoliticaContrasena> politicas = Collections.singletonList(
        new PoliticaLongitud(8, 16)
    );

    var exception = assertThrows(ValidacionContrasenaException.class,
        () -> getValidador(politicas).validar("corta"));

    List<PoliticaContrasena> politicasAValidar = exception.getPoliticasAValidar();
    assertEquals(1, politicasAValidar.size());
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaLongitud));
    assertTrue(exception.getMessage().contains(longitudMensajeError));
  }

  @Test
  public void testValidadorContrasenaLongitudValida() {
    List<PoliticaContrasena> politicas = Collections.singletonList(
        new PoliticaLongitud(8, 16)
    );

    assertDoesNotThrow(() -> getValidador(politicas).validar("longitudValida"));
  }

  @Test
  public void testValidadorContrasenaRegexInvalida() {
    List<PoliticaContrasena> politicas = Collections.singletonList(
        new PoliticaRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
            regexMensajeError)
    );

    var exception = assertThrows(ValidacionContrasenaException.class,
        () -> getValidador(politicas).validar("contraseña"));

    List<PoliticaContrasena> politicasAValidar = exception.getPoliticasAValidar();
    assertEquals(1, politicasAValidar.size());
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaRegex));
    assertTrue(exception.getMessage().contains(regexMensajeError));
  }

  @Test
  public void testValidadorContrasenaRegexValida() {
    List<PoliticaContrasena> politicas = Collections.singletonList(
        new PoliticaRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
            regexMensajeError)
    );

    assertDoesNotThrow(() -> getValidador(politicas).validar("Contraseña3*"));
  }

  @Test
  public void testValidadorContrasenaExcluidaInvalida() {
    List<PoliticaContrasena> politicas = Collections.singletonList(
        new PoliticaContrasenasExcluidas(new HashSet<>(Arrays.asList("contraseña", "123456")))
    );

    assertThrows(ValidacionContrasenaException.class,
        () -> getValidador(politicas).validar("contraseña"));

    var exception = assertThrows(ValidacionContrasenaException.class,
        () -> getValidador(politicas).validar("123456"));

    List<PoliticaContrasena> politicasAValidar = exception.getPoliticasAValidar();
    assertEquals(1, politicasAValidar.size());
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaContrasenasExcluidas));
    assertTrue(exception.getMessage().contains(excluidasMensajeError));
  }

  @Test
  public void testValidadorContrasenaExcluidaValida() {
    List<PoliticaContrasena> politicas = Collections.singletonList(
        new PoliticaContrasenasExcluidas(new HashSet<>(Arrays.asList("contraseña", "123456")))
    );

    assertDoesNotThrow(() -> getValidador(politicas).validar("noExcluida"));
  }

  @Test
  public void testValidadorContrasenaLongitudYRegexInvalidas() {
    List<PoliticaContrasena> longitudYRegex = Arrays.asList(
        new PoliticaLongitud(8, 16),
        new PoliticaRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
            regexMensajeError)
    );

    ValidacionContrasenaException exception = assertThrows(ValidacionContrasenaException.class,
        () -> getValidador(longitudYRegex).validar("abcd"));

    List<PoliticaContrasena> politicasAValidar = exception.getPoliticasAValidar();
    assertEquals(2, politicasAValidar.size());
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaLongitud));
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaRegex));
    assertTrue(exception.getMessage().contains(longitudMensajeError));
    assertTrue(exception.getMessage().contains(regexMensajeError));
  }

  @Test
  public void testValidadorContrasenaLongitudYRegexValidas() {
    List<PoliticaContrasena> longitudYRegex = Arrays.asList(
        new PoliticaLongitud(8, 16),
        new PoliticaRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
            regexMensajeError)
    );

    assertDoesNotThrow(() -> getValidador(longitudYRegex).validar("lonRegValida1!"));
  }

  @Test
  public void testValidadorContrasenaExcluidaYRegexInvalidas() {
    List<PoliticaContrasena> excluidaYRegex = Arrays.asList(
        new PoliticaRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
            regexMensajeError),
        new PoliticaContrasenasExcluidas(new HashSet<>(Arrays.asList("contraseña", "123456", "aA1!")))
    );

    ValidacionContrasenaException exception = assertThrows(ValidacionContrasenaException.class,
        () -> getValidador(excluidaYRegex).validar("contraseña"));

    List<PoliticaContrasena> politicasAValidar = exception.getPoliticasAValidar();
    assertEquals(2, politicasAValidar.size());
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaContrasenasExcluidas));
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaRegex));
    assertTrue(exception.getMessage().contains(excluidasMensajeError));
    assertTrue(exception.getMessage().contains(regexMensajeError));
  }

  @Test
  public void testValidadorContrasenaExcluidaYRegexValidas() {
    List<PoliticaContrasena> excluidaYRegex = Arrays.asList(
        new PoliticaRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
            regexMensajeError),
        new PoliticaContrasenasExcluidas(new HashSet<>(Arrays.asList("contraseña", "123456", "aA1!")))
    );

    assertDoesNotThrow(() -> getValidador(excluidaYRegex).validar("noExcl1+"));
  }

  @Test
  public void testValidadorContrasenaExcluidaYLongitudInvalidas() {
    List<PoliticaContrasena> excluidaYLongitud = Arrays.asList(
        new PoliticaLongitud(8, 16),
        new PoliticaContrasenasExcluidas(new HashSet<>(Arrays.asList("contraseña", "123456", "aA1!")))
    );

    ValidacionContrasenaException exception = assertThrows(ValidacionContrasenaException.class,
        () -> getValidador(excluidaYLongitud).validar("aA1!"));

    List<PoliticaContrasena> politicasAValidar = exception.getPoliticasAValidar();
    assertEquals(2, politicasAValidar.size());
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaContrasenasExcluidas));
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaLongitud));
    assertTrue(exception.getMessage().contains(excluidasMensajeError));
    assertTrue(exception.getMessage().contains(longitudMensajeError));
  }

  @Test
  public void testValidadorContrasenaExcluidaYLongitudValidas() {
    List<PoliticaContrasena> excluidaYLongitud = Arrays.asList(
        new PoliticaLongitud(8, 16),
        new PoliticaContrasenasExcluidas(new HashSet<>(Arrays.asList("contraseña", "123456", "aA1!")))
    );

    assertDoesNotThrow(() -> getValidador(excluidaYLongitud).validar("noExcluidaYLarga"));
  }

  @Test
  public void testValidadorContrasenaTodasLasPoliticasInvalidas() {
    ValidacionContrasenaException exception = assertThrows(ValidacionContrasenaException.class,
        () -> getValidador().validar("123456"));

    List<PoliticaContrasena> politicasAValidar = exception.getPoliticasAValidar();
    assertEquals(3, politicasAValidar.size());
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaContrasenasExcluidas));
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaLongitud));
    assertTrue(politicasAValidar.stream().anyMatch(politica -> politica instanceof PoliticaRegex));
    assertTrue(exception.getMessage().contains(excluidasMensajeError));
    assertTrue(exception.getMessage().contains(longitudMensajeError));
    assertTrue(exception.getMessage().contains(regexMensajeError));
  }

  @Test
  public void testValidadorContrasenaTodasLasPoliticasValida() {
    assertDoesNotThrow(() -> getValidador().validar("contraValida1+"));
  }

  private static ValidadorContrasena getValidador() {
    List<PoliticaContrasena> politicasDefault = Arrays.asList(
        new PoliticaLongitud(8, 16),
        new PoliticaRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
            regexMensajeError),
        new PoliticaContrasenasExcluidas(new HashSet<>(Arrays.asList("contraseña", "123456", "aA1!")))
    );

    return getValidador(politicasDefault);
  }

  private static ValidadorContrasena getValidador(List<PoliticaContrasena> politicas) {
    return new ValidadorContrasena(new ArrayList<>(politicas));
  }

  // TODO: de ser necesario, incorporarlo a PoliticaContrasenasExcluidas y parametrizar fileName
  private static HashSet<String> cargarPeoresContrasenas() throws IOException {
    var peoresContrasenas = new HashSet<String>();

    String projectDir = System.getProperty("user.dir");
    String filePath = Paths.get(projectDir, "src", "test", "resources", "top-10000-worst-passwords.txt").toString();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String contrasena;
      while ((contrasena = reader.readLine()) != null) {
        peoresContrasenas.add(contrasena);
      }
    } catch (IOException e) {
      throw new IOException(e);
    }

    return peoresContrasenas;
  }
}