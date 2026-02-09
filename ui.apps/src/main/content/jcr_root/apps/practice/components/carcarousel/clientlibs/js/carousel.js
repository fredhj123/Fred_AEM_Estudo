(function () {
  "use strict";
  
  console.log("A4X: O Script do Carrossel foi carregado!"); // <--- SE ISSO NÃO APARECER, A CLIENTLIB ESTÁ ERRADA

  function initCarousel(carousel) {
    console.log("A4X: Iniciando um carrossel...", carousel); // <--- SE ISSO NÃO APARECER, O HTML NÃO FOI ENCONTRADO
    
    const track = carousel.querySelector(".custom-carousel__track");
    const slides = carousel.querySelectorAll(".custom-carousel__slide");
  }
}
(function () {
  "use strict";

  // Função principal que inicializa CADA carrossel da página
  function initCarousel(carousel) {
    const track = carousel.querySelector(".custom-carousel__track");
    const slides = carousel.querySelectorAll(".custom-carousel__slide");

    // Segurança: Se não tem track ou tem menos de 2 slides, não faz nada
    if (!track || slides.length <= 1) {
      return;
    }

    let currentIndex = 0;
    const totalSlides = slides.length;
    const delay = 5000; // 5 segundos
    let autoSlideInterval;

    // Função que move o trilho (A lógica do translateX)
    function goToSlide(index) {
      // Move -100% para o slide 1, -200% para o slide 2, etc.
      track.style.transform = `translateX(-${index * 100}%)`;
      currentIndex = index;
    }

    // Função para calcular o próximo slide
    function nextSlide() {
      // O operador % (módulo) faz o loop infinito: 0 -> 1 -> 2 -> 0
      const newIndex = (currentIndex + 1) % totalSlides;
      goToSlide(newIndex);
    }

    // Controle do Auto-Play
    function startAutoSlide() {
      // Limpa qualquer intervalo anterior para evitar duplicação
      clearInterval(autoSlideInterval);
      autoSlideInterval = setInterval(nextSlide, delay);
    }

    function stopAutoSlide() {
      clearInterval(autoSlideInterval);
    }

    // Inicia o carrossel
    startAutoSlide();

    // UX: Pausa quando o mouse passa por cima (Importante para leitura)
    carousel.addEventListener('mouseenter', stopAutoSlide);
    carousel.addEventListener('mouseleave', startAutoSlide);
  }

  // Inicializa quando o DOM estiver pronto
  document.addEventListener("DOMContentLoaded", function () {
    // Procura por TODOS os carrosséis na página e inicia um por um
    const carousels = document.querySelectorAll(".custom-carousel");
    carousels.forEach(initCarousel);
  });
})();