
document.addEventListener('DOMContentLoaded', function () {
    const closeButton = document.querySelector('[data-bs-target="#reportarIncidenteModal"]');
        const form = document.querySelector('#reportarIncidenteModal form');

    document.addEventListener('click', function (event) {
      if (event.target && event.target.getAttribute('data-servicio-id')) {
              const servicioId = event.target.getAttribute('data-servicio-id');

              form.action = `incidentes/nuevo?servicio=${servicioId}&from=servicios`;
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