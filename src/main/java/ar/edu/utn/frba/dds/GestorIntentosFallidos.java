package ar.edu.utn.frba.dds;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class GestorIntentosFallidos {
  private static int intentosFallidos;
  private static int tiempoEspera;
  private static Timer timer;
  private static boolean puedeReintentar = true;

  public static void registrarIntentoFallido() {
    if(puedeReintentar) {
      puedeReintentar = false;
      intentosFallidos++;
      tiempoEspera = 5 * intentosFallidos; // Aumentar el tiempo de espera con cada intento fallido
      System.out.printf("Por favor, esperá %d segundos antes de intentar nuevamente.%n", tiempoEspera);
      timer = new Timer();
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          System.out.println("Ya podés intentar nuevamente.");
          puedeReintentar = true;
          tiempoEspera = 0;
          timer.cancel();
        }
      }, (long) tiempoEspera * 1000);
    } else {
      System.out.printf("Por favor, esperá antes de intentar nuevamente.%n");
    }
  }
}