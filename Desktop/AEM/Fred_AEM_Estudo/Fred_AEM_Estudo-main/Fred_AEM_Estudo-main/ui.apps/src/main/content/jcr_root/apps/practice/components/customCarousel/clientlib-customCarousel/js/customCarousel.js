(function () {
  "use strict";

  document.addEventListener("DOMContentLoaded", function () {

    document.querySelectorAll(".custom-carousel-fred").forEach(function (carousel) {

      const slides = carousel.querySelectorAll(".carousel-slide-fred");
      let index = 0;

      if (slides.length === 0) return;

      setInterval(() => {
        slides[index].classList.remove("active");
        index = (index + 1) % slides.length;
        slides[index].classList.add("active");
      }, 3000);

    });
 
  });
})();
