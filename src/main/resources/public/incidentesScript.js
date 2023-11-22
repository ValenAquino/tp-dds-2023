
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
        else if (from == 'pendientes')
            form.action = `comunidades/${comunidadId}/incidentes/${incidentId}?from=pendientes`;
        else
            form.action = `incidentes/${incidentId}?from=incidentes`;
      }
    });

    const iconButtons = document.querySelectorAll(".icon-button");

    // Iterate over the selected buttons and add event listeners
    iconButtons.forEach(function(iconButton) {
        const icon1 = iconButton.querySelector(".display-icon");
        const icon2 = iconButton.querySelector(".hover-icon");

        iconButton.addEventListener("mouseover", function() {
            icon1.style.display = "none";
            icon2.style.display = "inline";
        });

        iconButton.addEventListener("mouseout", function() {
            icon1.style.display = "inline";
            icon2.style.display = "none";
        });
    });
});