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

  public static Helper<Usuario> formatearUsuario = (usuario, options) -> usuario.getUsuario();

  public static Helper<Boolean> formatearEsAdmin = ((esAdmin, options) -> esAdmin ? "Sí" : "No");
}