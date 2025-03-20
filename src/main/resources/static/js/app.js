console.log('solar-energia\nweb-store-app\n©2025 unexcoder');

function toggleNextField() {
    let password = document.getElementById("password").value;
    let passwordRepeat = document.getElementById("passwordRepeat");
    // passwordRepeat.disabled = !password;
    if (!password) {
        passwordRepeat.style.display = "block";
    } else {
        passwordRepeat.style.display = "none";
    }
}

function previewImage(event) {
    var reader = new FileReader();
    reader.onload = function () {
        var img = document.getElementById('displayImage');
        img.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
}