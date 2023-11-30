document.addEventListener("DOMContentLoaded", function() {
    var editarBoton = document.getElementById('editarBoton');
    var cancelarBoton = document.getElementById('cancelarBoton');
    var formularioPerfil = document.getElementById('formularioPerfil');
    disableForm();

    editarBoton.addEventListener('click', function(e) {
        e.preventDefault();
        enableForm();
    });
    cancelarBoton.addEventListener('click', function() {
        disableForm();
    });

    function enableForm() {
        formularioPerfil.querySelectorAll('input, textarea').forEach(function(element) {
            if (element !== editarBoton && element !== cancelarBoton) {
                element.removeAttribute('disabled');
            }
        });
        editarBoton.value = 'Confirmar';
        editarBoton.classList.add('confirmar');
        editarBoton.removeEventListener('click', enableForm);
        editarBoton.addEventListener('click', function() {
            formularioPerfil.submit();
        });
        cancelarBoton.classList.remove('disabled');
    }

    function disableForm() {
        formularioPerfil.querySelectorAll('input, textarea').forEach(function(element) {
            if (element !== editarBoton && element !== cancelarBoton) {
                element.setAttribute('disabled', 'true');
            }
        });
        editarBoton.value = 'Editar';
        editarBoton.classList.remove('confirmar');
        editarBoton.removeEventListener('click', enableForm);
        editarBoton.addEventListener('click', function(e) {
            e.preventDefault();
            enableForm();
        });
        cancelarBoton.classList.add('disabled');
    }
});
