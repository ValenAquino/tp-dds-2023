
document.addEventListener('DOMContentLoaded', function () {
    const closeButton = document.querySelector('[data-bs-target="#confirmarEliminarModal"]');
    const form = document.querySelector('#confirmarEliminarModal form');

    document.addEventListener('click', function (event) {
      if (event.target && event.target.getAttribute('data-usuario-id')) {
        const usuarioId = event.target.getAttribute('data-usuario-id');

        //form.method = "DELETE";
        form.action = `usuarios/eliminar?id=${usuarioId}`;
      }
    });
});