package ar.edu.utn.frba.dds.model.entidades.rankings;

import ar.edu.utn.frba.dds.model.entidades.Entidad;
import ar.edu.utn.frba.dds.model.entidades.Incidente;
import java.util.List;
import java.util.Map;

public interface CriterioDeOrdenamiento {
  Map<Entidad, Double> getEntidadesOrdenadas(List<Incidente> incidentes);

  String getDescripcion();
}