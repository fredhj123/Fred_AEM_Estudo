(function () {
  "use strict";

  function initCarousel(carousel) {
    const track = carousel.querySelector(".custom-carousel__track");
    const slides = carousel.querySelectorAll(".custom-carousel__slide");

    if (!track || slides.length <= 1) {
      return;
    }

    let currentIndex = 0;
    const totalSlides = slides.length;
    const delay = 5000; // 2 seconds

    function goToSlide(index) {
      track.style.transform = `translateX(-${index * 100}%)`;
    }

    function nextSlide() {
      currentIndex = (currentIndex + 1) % totalSlides;
      goToSlide(currentIndex);
    }

    // Initial position
    goToSlide(currentIndex);

    // Auto-play
    setInterval(nextSlide, delay);
  }

  document.addEventListener("DOMContentLoaded", function () {
    document
      .querySelectorAll(".custom-carousel")
      .forEach(initCarousel);
  });
})();
