
window.addEventListener('DOMContentLoaded', function () {
    // Intercept all form submissions
    document.querySelectorAll('form').forEach(form => {
    const formMethod = form.getAttribute('method');
        if (formMethod && ['PUT', 'PATCH', 'DELETE'].includes(formMethod)) {
            form.addEventListener('submit', function(event) {
                event.preventDefault();
                let method = this.getAttribute('method');
                let action = this.action;
                let options = { method: method };

                if (method === 'PATCH' || method === 'PUT') {
                    let formData = new FormData(this);
                    options.body = formData;
                }

                fetch(action, options)
                .then(response => {
                    if (response.redirected) {
                        window.location.href = response.url;
                    }
                })
                .catch(error => {
                    console.error(error);
                });

                return false;
            });
        }
    });

    const closeButton = document.querySelector('[data-bs-target="#confirmarCierreModal"]');
    const modalForm = document.querySelector('#confirmarCierreModal form');

    document.addEventListener('click', function (event) {
      if (event.target && event.target.getAttribute('data-incident-id')) {
        const incidentId = event.target.getAttribute('data-incident-id');

        const from = event.target.getAttribute('data-from');
        const comunidadId = event.target.getAttribute('data-comunidad-id');

        if (from == 'index')
            modalForm.action = `comunidades/${comunidadId}/incidentes/${incidentId}?from=index`;
        else if (from == 'pendientes')
            modalForm.action = `comunidades/${comunidadId}/incidentes/${incidentId}?from=pendientes`;
        else
            modalForm.action = `incidentes/${incidentId}?from=incidentes`;
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

    const iconLinks = document.querySelectorAll(".icon-link");

    // Iterate over the selected buttons and add event listeners
    iconLinks.forEach(function(iconButton) {
        const icon1 = iconButton.querySelector(".comunidad-display-icon");
        const icon2 = iconButton.querySelector(".comunidad-hover-icon");

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