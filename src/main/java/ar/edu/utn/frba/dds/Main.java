package ar.edu.utn.frba.dds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Main {

  private static final Set<String> peoresContrasenas = cargarPeoresContrasenas();
  private static final int MAX_INTENTOS_FALLIDOS = 5;
  private static int intentosFallidos = 0;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Ingresar nombre de usuario: ");
    String usuario = scanner.nextLine();
    System.out.print("Ingresar contraseña: ");
    String contrasena = scanner.nextLine();

    while (!esContrasenaValida(contrasena)) {
      System.out.println("Contraseña inválida. Ingresá una contraseña que cumpla con los requisitos: ");
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

    if (!cumpleNIST800_63(contrasena)) {
      System.out.println("La contraseña ingresada no cumple con las recomendaciones de la Guía NIST 800-63.");
      return false;
    }

    return true;
  }

  private static boolean cumpleNIST800_63(String contrasena) {
    // Longitud mínima de 8 caracteres y máxima de 64 caracteres
    if (contrasena.length() < 8 || contrasena.length() > 64) {
      return false;
    }

    // Al menos un carácter en minúscula, uno en mayúscula, un número y un carácter especial
    Pattern patron = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-+_!@#$%^&*.,?]).+$");
    return patron.matcher(contrasena).matches();
  }

  private static Set<String> cargarPeoresContrasenas() {
    Set<String> peoresContrasenas = new HashSet<>();

    try {
      InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("top-10000-worst-passwords.txt");
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
      System.err.println("Error al cargar el archivo de contraseñas: " + e.getMessage());
    }

    return peoresContrasenas;
  }

  public static void login(String usuario, String contrasena) {
    // Acá verificaríamos si el usuario y la contraseña son correctos
    boolean esCorrecto = true; // Y acá eventualmente iría el chequeo :)

    // Esto estaría bueno hacerlo con ´timers´ y no con un sleep así no se pausa todo

    if (!esCorrecto) {
      intentosFallidos++;

      if (intentosFallidos >= MAX_INTENTOS_FALLIDOS) {
        try {
          System.out.println("Demasiados intentos fallidos. Por favor, esperá 30 segundos antes de intentar nuevamente.");
          TimeUnit.SECONDS.sleep(30);
          intentosFallidos = 0; // Reiniciar el contador de intentos fallidos después de la espera
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } else {
        int tiempoEspera = 5 * intentosFallidos; // Aumentar el tiempo de espera con cada intento fallido
        System.out.println("Credenciales incorrectas. Por favor, espere " + tiempoEspera + " segundos antes de intentar nuevamente.");
        try {
          TimeUnit.SECONDS.sleep(tiempoEspera);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    } else {
      System.out.println("Inicio de sesión exitoso.");
      intentosFallidos = 0; // Reiniciar el contador de intentos fallidos después de un inicio de sesión exitoso
    }
  }
}