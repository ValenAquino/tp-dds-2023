
document.addEventListener('DOMContentLoaded', function () {
    const closeButton = document.querySelector('[data-bs-target="#confirmarCierreModal"]');
    const form = document.querySelector('#confirmarCierreModal form');

    document.addEventListener('click', function (event) {
      if (event.target && event.target.getAttribute('data-incident-id')) {
        const incidentId = event.target.getAttribute('data-incident-id');

        const from = event.target.getAttribute('data-from');
        const comunidadId = event.target.getAttribute('data-comunidad-id');

        if (from == 'index')
            form.action = `home/comunidades/${comunidadId}/incidentes/${incidentId}?from=index`;
        else
            form.action = `incidentes/${incidentId}?from=incidentes`;
      }
    });
});