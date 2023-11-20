package spark.template.handlebars;

import ar.edu.utn.frba.dds.model.entidades.Usuario;
import com.github.jknack.handlebars.Helper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HandlebarsHelpers {

  public static Helper<LocalDateTime> formatearFecha = (fecha, options) -> {
    if (fecha == null) {
      return "";
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    return fecha.format(formatter);
  };

  public static Helper<Usuario> formatearUsuario = (usuario, options) -> usuario != null ? usuario.getUsuario() : "Desconocido";

  public static Helper<Boolean> formatearEsAdmin = ((esAdmin, options) -> esAdmin ? "Sí" : "No");

  public static Helper<String> stringIsEqual = (str1, options) -> {
    String str2 = options.param(0);
    return str1.equals(str2) ? options.fn() : options.inverse();
  };

  public static Helper<Integer> intIsEqual = (int1, options) -> {
    Integer int2 = options.param(0);
    return int1.equals(int2) ? options.fn() : options.inverse();
  };
}
