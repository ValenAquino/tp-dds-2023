package ar.edu.utn.frba.dds.entidades.rankings;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import java.util.ArrayList;
import java.util.List;

public class GeneradorRankingSemanal {
  OrganismoDeControl organismoDeControl;
  List<Ranking> rankings;
  List<CriterioDeOrdenamiento> criterios;

  public GeneradorRankingSemanal(OrganismoDeControl organismoDeControl) {
    this.organismoDeControl = organismoDeControl;
    this.rankings = new ArrayList<>();
    this.criterios = new ArrayList<>();
  }

  public void agregarCriterio(CriterioDeOrdenamiento criterio) {
    criterios.add(criterio);
  }

  public void generarRanking(CriterioDeOrdenamiento criterio) {
    List<Entidad> entidades = organismoDeControl.getEntidadesUltimaSemana();

  }

  public void generarRankingSemanal() {
    for (CriterioDeOrdenamiento criterio : criterios) {
      generarRanking(criterio);
    }
  }
}
