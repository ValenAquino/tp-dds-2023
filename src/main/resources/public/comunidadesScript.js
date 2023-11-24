
document.addEventListener('DOMContentLoaded', function () {
    const closeButton = document.querySelector('[data-bs-target="#confirmarEliminarModal"]');
    const form = document.querySelector('#confirmarEliminarModal form');

    document.addEventListener('click', function (event) {
      if (event.target && event.target.getAttribute('data-comunidad-id')) {
        const comunidadId = event.target.getAttribute('data-comunidad-id');

        //form.method = "DELETE";
        form.action = `comunidades/eliminar?id=${comunidadId}`;
      }
    });
});