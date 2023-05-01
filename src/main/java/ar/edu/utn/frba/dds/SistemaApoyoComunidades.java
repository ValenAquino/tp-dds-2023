package ar.edu.utn.frba.dds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;


public class SistemaApoyoComunidades {

  private static final Set<String> peoresContrasenas = cargarPeoresContrasenas();

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Ingresá un nombre de usuario: ");
    String usuario = scanner.nextLine();
    System.out.print("Ingresá una contraseña: ");
    String contrasena = scanner.nextLine();

    while (!esContrasenaValida(contrasena)) {
      System.out.println("Ingresá una contraseña que cumpla con los requisitos: ");
      contrasena = scanner.nextLine();
    }

    // Acá guardaríamos el usuario y la contraseña en una base de datos o en algún otro lado.
    System.out.println("Usuario registrado exitosamente.");
  }

  private static boolean esContrasenaValida(String contrasena) {
    if (peoresContrasenas.contains(contrasena)) {
      System.out.println("La contraseña ingresada se encuentra en la lista de las peores contraseñas. Por favor, ingresá otra.");
      return false;
    }

    return cumpleNIST800_63(contrasena);
  }

  private static boolean cumpleNIST800_63(String contrasena) {
    if (contrasena.length() < 8 || contrasena.length() > 64) {
      System.out.println("La contraseña ingresada debe tener una longitud mínima de 8 caracteres y máxima de 64 caracteres");
      return false;
    }

    Pattern patron = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$");
    if (!patron.matcher(contrasena).matches()) {
      System.out.println("La contraseña ingresada debe tener al menos un carácter en minúscula, uno en mayúscula, un número y un carácter especial");
      return false;
    }

    return true;
  }

  private static Set<String> cargarPeoresContrasenas() {
    Set<String> peoresContrasenas = new HashSet<>();

    try {
      InputStream inputStream = SistemaApoyoComunidades.class.getClassLoader().getResourceAsStream("top-10000-worst-passwords.txt");
      if (inputStream == null) {
        throw new IOException("No se pudo encontrar el archivo 'top-10000-worst-passwords.txt'");
      }

      // Leer el archivo y cargar las contraseñas en el HashSet
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        String contrasena;
        while ((contrasena = reader.readLine()) != null) {
          peoresContrasenas.add(contrasena);
        }
      }
    } catch (IOException e) {
      System.err.println("Error al cargar el archivo de peores contraseñas: " + e.getMessage());
    }

    return peoresContrasenas;
  }

  public static void login() {
    Scanner scanner = new Scanner(System.in);
    String usuario = scanner.nextLine();
    String contrasena = scanner.nextLine();

    while(!verificarCredenciales(usuario, contrasena)) {
      GestorIntentosFallidos.registrarIntentoFallido();
      contrasena = scanner.nextLine();
    }
  }

  private static boolean verificarCredenciales(String usuario, String contrasena) {
    return false;
  }
}