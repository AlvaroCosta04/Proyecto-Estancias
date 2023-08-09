/*function validar() {
    var contrasenia = document.getElementById("contrasenia").value;
    var confirmarContrasenia = document.getElementById("confirmar_contrasenia").value;

    if (contrasenia !== confirmarContrasenia) {
        alert("Las contraseñas no coinciden. Por favor, verifícalas y vuelve a intentarlo.");
        return false;
    }

    return true;
}*/

function previsualizarImagen(event) {
    var input = event.target;
    var reader = new FileReader();
    reader.onload = function () {
        var dataURL = reader.result;
        var img = document.createElement("img");
        img.src = dataURL;
        var previsualizacion = document.getElementById("previsualizacion");
        previsualizacion.innerHTML = '';
        previsualizacion.appendChild(img);
    };
    reader.readAsDataURL(input.files[0]);
}

function previsualizarImagen(event) {
    var input = event.target;
    var file = input.files[0];

    var compressor = new Compressor(file, {
        quality: 0.6,
        maxWidth: 800,
        maxHeight: 800,
        success: function (compressedResult) {
            var reader = new FileReader();
            reader.onload = function () {
                var dataURL = reader.result;
                var img = document.createElement("img");
                img.src = dataURL;
                var previsualizacion = document.getElementById("previsualizacion");
                previsualizacion.innerHTML = '';
                previsualizacion.appendChild(img);
            };
            reader.readAsDataURL(compressedResult);
        },
        error: function (error) {
        }
    });
}
