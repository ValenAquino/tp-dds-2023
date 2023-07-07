package ar.edu.utn.frba.dds.entidades.rankings;

import ar.edu.utn.frba.dds.entidades.Entidad;
import java.util.List;

public interface CriterioDeOrdenamiento {
  List<Entidad> getEntidadesOrdenadas();
}