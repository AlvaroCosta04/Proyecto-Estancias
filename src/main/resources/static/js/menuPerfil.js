/*function toggleProfileMenu() {
  var menu = document.getElementById("profile-menu");
  if (menu.style.display === "block") {
    menu.style.display = "none";
  } else {
    menu.style.display = "block";
  }
}

window.onclick = function(event) {
  if (!event.target.matches('.profile-btn')) {
    var menu = document.getElementById("profile-menu");
    if (menu.style.display === "block") {
      menu.style.display = "none";
    }
  }
}*/

const dropdownBtn = document.querySelector(".dropdown-btn");
const dropdown = document.querySelector(".dropdown");

dropdownBtn.addEventListener("click", () => {
  dropdown.classList.toggle("show");
});

window.addEventListener("click", (e) => {
  if (!e.target.matches(".dropdown-btn")) {
    dropdown.classList.remove("show");
  }
});