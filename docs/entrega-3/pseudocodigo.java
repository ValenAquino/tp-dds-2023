// Comunidad
void abrirIncidente(Servicio servicio) {
  var incidente = new Incidente(servicio, "")
  incidentes.add(incidente)
  this.getUsuariosInteresados(servicio)
    .forEach(u -> u.notificarIncidente(incidente))
}

List<Usuario> getUsuariosInteresados(Servicio servicio) {
  return usuarios.filter(u -> u.interesadoEn(servicio))
}

void cerrarIncidente(Incidente incidente) {
  var incidente = incidentes.find(i -> i.equals(incidente))
  incidente.setResuelto(true)
  incidente.fechaResolucion = LocalDateTime.now()
}

// Usuario
bool interesadoEn(Servicio servicio) {
  return entidadesDeInteres.any(e -> e.tieneServicio(servicio))
}

void notificarIncidente(Incidente incidente) {
  if (calendarioNotificaciones.abarcaA(incidente.getFecha())) {
    medioDeComunicacion.notificar()
  }
}

// Entidad
bool tieneServicio(Servicio servicio) {
  return establecimientos.any(e -> e.tieneServicio(servicio))
}

// Establecimiento
bool tieneServicio(Servicio servicio) {
  return servicios.any(s -> s.equals(servicio))
}

class RangoHorario {
    private LocalTime inicio;
    private LocalTime fin;

    public RangoHorario(LocalTime inicio, LocalTime fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public boolean contiene(LocalTime hora) {
        return hora.equals(inicio) || hora.isAfter(inicio) && hora.isBefore(fin);
    }
}

class CalendarioNotificaciones {
    private Map<DayOfWeek, RangoHorario> horarios;

    public CalendarioNotificaciones() {
        this.horarios = new HashMap<>();
    }

    public void agregarHorario(DayOfWeek diaSemana, LocalTime inicio, LocalTime fin) {
        horarios.put(diaSemana, new RangoHorario(inicio, fin));
    }

    public void eliminarHorario(DayOfWeek diaSemana) {
        horarios.remove(diaSemana);
    }

    public boolean abarcaA(DayOfWeek diaSemana, LocalTime hora) {
        RangoHorario rango = horarios.get(diaSemana);
        return rango != null && rango.contiene(hora);
    }
}