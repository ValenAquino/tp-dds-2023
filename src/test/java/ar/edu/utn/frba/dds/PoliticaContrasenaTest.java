package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.politicas.PoliticaContrasenasExcluidas;
import ar.edu.utn.frba.dds.politicas.PoliticaLongitud;
import ar.edu.utn.frba.dds.politicas.PoliticaRegex;
import ar.edu.utn.frba.dds.validacion.PoliticaContrasena;
import ar.edu.utn.frba.dds.validacion.ValidacionContrasenaException;
import ar.edu.utn.frba.dds.validacion.ValidadorContrasena;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.fail;

public class PoliticaContrasenaTest {
  @Test
  public void testPoliticaLongitudValida() {
    PoliticaLongitud politica = new PoliticaLongitud(8, 16);

    try {
      politica.validar("contraseña");
      // Si llega hasta acá, la contraseña es válida
    } catch (ValidacionContrasenaException e) {
      fail("La contraseña debería ser válida");
    }
  }

  @Test(expected = ValidacionContrasenaException.class)
  public void testPoliticaLongitudInvalida() {
    PoliticaLongitud politica = new PoliticaLongitud(8, 16);
    politica.validar("corta");
  }

  @Test
  public void testPoliticaRegexValida() {
    PoliticaRegex politica = new PoliticaRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$", "La contraseña no cumple con los requisitos de seguridad");

    try {
      politica.validar("Contraseña1!");
      // Si llega hasta acá, la contraseña es válida
    } catch (ValidacionContrasenaException e) {
      fail("La contraseña debería ser válida");
    }
  }

  @Test(expected = ValidacionContrasenaException.class)
  public void testPoliticaRegexInvalida() {
    PoliticaRegex politica = new PoliticaRegex(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$",
        "La contraseña debe tener menos un carácter en minúscula, uno en mayúscula, un número y un carácter especial"
    );
    politica.validar("contraseña");
  }

  @Test
  public void testPoliticaContrasenasExcluidasValida() {
    Set<String> contrasenasExcluidas = new HashSet<>();
    contrasenasExcluidas.add("contraseña");
    PoliticaContrasenasExcluidas politica = new PoliticaContrasenasExcluidas(contrasenasExcluidas);

    try {
      politica.validar("otraContraseña");
      // Si llega hasta acá, la contraseña es válida
    } catch (ValidacionContrasenaException e) {
      fail("La contraseña debería ser válida");
    }
  }

  @Test(expected = ValidacionContrasenaException.class)
  public void testPoliticaContrasenasExcluidasInvalida() {
    Set<String> contrasenasExcluidas = new HashSet<>();
    contrasenasExcluidas.add("contraseña");
    PoliticaContrasenasExcluidas politica = new PoliticaContrasenasExcluidas(contrasenasExcluidas);
    politica.validar("contraseña");
  }

  @Test
  public void testValidadorContrasenaValida() {
    Set<String> contrasenasExcluidas = Collections.singleton("contraseña");

    List<PoliticaContrasena> politicas = new ArrayList<>();
    politicas.add(new PoliticaLongitud(8, 16));
    politicas.add(new PoliticaRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$", "La contraseña no cumple con los requisitos de seguridad"));
    politicas.add(new PoliticaContrasenasExcluidas(contrasenasExcluidas));

    ValidadorContrasena validador = new ValidadorContrasena(politicas);

    try {
      validador.validar("Contraseña1!");
      // Si llega hasta acá, la contraseña es válida
    } catch (ValidacionContrasenaException e) {
      fail("La contraseña debería ser válida");
    }
  }

  @Test(expected = ValidacionContrasenaException.class)
  public void testValidadorContrasenaInvalida() {
    Set<String> contrasenasExcluidas = Collections.singleton("contraseña");

    List<PoliticaContrasena> politicas = new ArrayList<>();
    politicas.add(new PoliticaLongitud(8, 16));
    politicas.add(new PoliticaRegex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$", "La contraseña no cumple con los requisitos de seguridad"));
    politicas.add(new PoliticaContrasenasExcluidas(contrasenasExcluidas));

    ValidadorContrasena validador = new ValidadorContrasena(politicas);

    validador.validar("contraseña");
  }
}
