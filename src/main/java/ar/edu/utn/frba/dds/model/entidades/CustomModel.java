package ar.edu.utn.frba.dds.model.entidades;

import java.util.HashMap;
import spark.Request;
public class CustomModel extends HashMap<String, Object> {
    public CustomModel(String titulo, Request request) {
        super();
        this.put("es_admin", request.session().attribute("is_admin"));
        this.put("nombre_usuario", request.session().attribute("nombre_usuario"));
        this.put("titulo", titulo);
    }
}
