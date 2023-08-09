const slides = document.querySelectorAll(".slide");

slides.forEach((slide) => {
    const image = slide.querySelector("img");
    const button = slide.querySelector("button");

    button.addEventListener("click", () => {
        const src = image.getAttribute("src");
        const fullImage = document.createElement("img");
        fullImage.setAttribute("src", src);

        const overlay = document.createElement("div");
        overlay.classList.add("overlay");

        const closeBtn = document.createElement("button");
        closeBtn.classList.add("close-btn");
        closeBtn.innerText = "Cerrar";

        overlay.appendChild(fullImage);
        overlay.appendChild(closeBtn);
        document.body.appendChild(overlay);

        closeBtn.addEventListener("click", () => {
            overlay.remove();
        });
    });
});