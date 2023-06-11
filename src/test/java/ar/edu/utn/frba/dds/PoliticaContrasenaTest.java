package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.password.politicas.PoliticaContrasenasExcluidas;
import ar.edu.utn.frba.dds.password.politicas.PoliticaLongitud;
import ar.edu.utn.frba.dds.password.politicas.PoliticaRegex;
import ar.edu.utn.frba.dds.password.validacion.PoliticaContrasena;
import ar.edu.utn.frba.dds.excepciones.ValidacionContrasenaException;
import ar.edu.utn.frba.dds.password.validacion.ValidadorContrasena;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PoliticaContrasenaTest {

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
        "La contraseña debe tener al menos una letra minúscula, una mayúscula, un número y un caracter especial."
    );

    assertTrue(politica.esValida("Contraseña1!"));
  }

  @Test
  public void testPoliticaRegexInvalida() {
    PoliticaRegex politica = new PoliticaRegex(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
        "La contraseña debe tener al menos una letra minúscula, una mayúscula, un número y un caracter especial."
    );

    assertFalse(politica.esValida("contraseña"));
  }

  @Test
  public void testPoliticaContrasenasExcluidasValida() {
    Set<String> contrasenasExcluidas = new HashSet<>();
    contrasenasExcluidas.add("contraseña");
    PoliticaContrasenasExcluidas politica = new PoliticaContrasenasExcluidas(contrasenasExcluidas);

    assertTrue(politica.esValida("otraContraseña"));
  }

  @Test
  public void testPoliticaContrasenasExcluidasInvalida() {
    Set<String> contrasenasExcluidas = new HashSet<>();
    contrasenasExcluidas.add("contraseña");
    PoliticaContrasenasExcluidas politica = new PoliticaContrasenasExcluidas(contrasenasExcluidas);

    assertFalse(politica.esValida("contraseña"));
  }

  @Test
  public void testValidadorContrasenaValida() {
    Set<String> contrasenasExcluidas = Collections.singleton("contraseña");

    List<PoliticaContrasena> politicas = new ArrayList<>();
    politicas.add(new PoliticaLongitud(8, 16));
    politicas.add(new PoliticaRegex(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
        "La contraseña debe tener al menos un carácter en minúscula, uno en mayúscula, un número y un carácter especial"));
    politicas.add(new PoliticaContrasenasExcluidas(contrasenasExcluidas));

    ValidadorContrasena validador = new ValidadorContrasena(politicas);

    assertDoesNotThrow(() -> validador.validar("Contraseña1!"));
  }

  @Test
  public void testValidadorContrasenaInvalida() {
    Set<String> contrasenasExcluidas = Collections.singleton("contraseña");

    List<PoliticaContrasena> politicas = new ArrayList<>();
    politicas.add(new PoliticaLongitud(8, 16));
    politicas.add(new PoliticaRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$", "La contraseña no cumple con los requisitos de seguridad"));
    politicas.add(new PoliticaContrasenasExcluidas(contrasenasExcluidas));

    ValidadorContrasena validador = new ValidadorContrasena(politicas);

    assertThrows(ValidacionContrasenaException.class, () -> validador.validar("contraseña"));
  }
}