function searchProvince() {
    var selectedProvince = document.getElementById("provinceInput").value;
    if (selectedProvince) {
        var url = "/view-reserve/province-" + selectedProvince;
        window.location.href = url;
    }
}

