package ar.edu.utn.frba.dds.entidades.rankings;

import ar.edu.utn.frba.dds.entidades.Entidad;
import ar.edu.utn.frba.dds.entidades.OrganismoDeControl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GeneradorRankingSemanal {
  /* TODO: Implementar RepoOrganismos para obtener todos los organismo de control
      Se debería crear el repo en el momento que se cargan los csv */
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
    // Capaz sea mejor pedir la lisa de incidentes de una para no romper tanto el encapsulamiento
    // Cuando esté el repoOrganismos, el organismo se pasa por parámetro
    List<Entidad> entidades = organismoDeControl.getEntidades();
    criterio.ordenar(entidades);
    Ranking ranking = new Ranking(LocalDateTime.now(), criterio.getNombre(), entidades);
    rankings.add(ranking);
  }

  public void generarRankingSemanal() {
    for (CriterioDeOrdenamiento criterio : criterios) {
      generarRanking(criterio);
    }
  }

  public List<Ranking> getRankings() {
    return rankings;
  }
}
