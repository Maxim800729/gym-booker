// main.js — общий скрипт сайта

document.addEventListener('DOMContentLoaded', () => {
    // ----- ТРЕНЕРЫ -----
    const trainerListEl = document.getElementById('trainerList');
    if (trainerListEl && window.Trainers && typeof window.Trainers.init === 'function') {
        window.Trainers.init();
    }

    // ----- МОИ ЗАПИСИ -----
    const myShowBtn = document.getElementById('my-show-btn');
    if (myShowBtn && window.Bookings && typeof window.Bookings.init === 'function') {
        window.Bookings.init();
    }

    // ----- ГАЛЕРЕЯ -----
    const GALLERY = [
        '/img/gallery/g-1.jpg',
        '/img/gallery/g-2.jpg',
        '/img/gallery/g-3.jpg',
        '/img/gallery/g-4.jpg',
        '/img/gallery/g-5.jpg',
        '/img/gallery/g-6.webp', // webp ок для Chrome/Edge
        '/img/gallery/g-7.jpg',
        '/img/gallery/g-8.webp'
    ];

    const galleryBox = document.getElementById('gallery');
    if (galleryBox) {
        renderGallery(galleryBox, GALLERY);
        enableLightbox(galleryBox, GALLERY);
    }
});

/* ---------- helpers ---------- */

// Рендер карточек галереи
function renderGallery(container, images) {
    container.innerHTML = images
        .map((src, i) => `
      <figure class="gal-card" style="margin:0">
        <img class="gal-img" data-index="${i}" loading="lazy"
             src="${src}" alt="" style="
             width:100%;height:260px;object-fit:cover;
             border-radius:12px;border:1px solid #22252d;">
      </figure>
    `)
        .join('');
}

// Лайтбокс (просмотр во весь экран)
function enableLightbox(container, images) {
    let current = 0;
    let overlay = null;
    let imgEl = null;

    // Создаём оверлей один раз
    function createOverlay() {
        overlay = document.createElement('div');
        overlay.id = 'lightbox';
        overlay.setAttribute('role', 'dialog');
        overlay.setAttribute('aria-modal', 'true');
        overlay.style.cssText = `
      position:fixed;inset:0;z-index:9999;
      display:flex;align-items:center;justify-content:center;
      background:rgba(0,0,0,.85);
    `;

        imgEl = document.createElement('img');
        imgEl.style.cssText = `
      max-width:92vw;max-height:92vh;border-radius:12px;
      box-shadow:0 20px 60px rgba(0,0,0,.6);
    `;

        // Кнопки навигации
        const btnPrev = document.createElement('button');
        const btnNext = document.createElement('button');
        [btnPrev, btnNext].forEach(b => {
            b.style.cssText = `
        position:fixed;top:50%;transform:translateY(-50%);
        background:rgba(255,255,255,.12);color:#fff;border:1px solid rgba(255,255,255,.25);
        padding:10px 14px;border-radius:10px;cursor:pointer;font-weight:800;
      `;
        });
        btnPrev.style.left = '20px';
        btnNext.style.right = '20px';
        btnPrev.textContent = '←';
        btnNext.textContent = '→';

        btnPrev.addEventListener('click', e => { e.stopPropagation(); show(current - 1); });
        btnNext.addEventListener('click', e => { e.stopPropagation(); show(current + 1); });

        // Закрытие по клику по фону
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) close();
        });

        // Навигация с клавиатуры
        function onKey(e) {
            if (e.key === 'Escape') close();
            if (e.key === 'ArrowLeft') show(current - 1);
            if (e.key === 'ArrowRight') show(current + 1);
        }
        overlay._onKey = onKey;

        overlay.append(imgEl, btnPrev, btnNext);
        document.body.appendChild(overlay);
        document.addEventListener('keydown', onKey);
    }

    function show(idx) {
        if (idx < 0) idx = images.length - 1;
        if (idx >= images.length) idx = 0;
        current = idx;
        imgEl.src = images[current];
    }

    function open(idx) {
        if (!overlay) createOverlay();
        overlay.style.display = 'flex';
        show(idx);
    }

    function close() {
        if (!overlay) return;
        overlay.style.display = 'none';
        document.removeEventListener('keydown', overlay._onKey);
        // Повесим снова при следующем открытии
        document.addEventListener('keydown', overlay._onKey);
    }

    // Делегируем клик по миниатюрам
    container.addEventListener('click', (e) => {
        const img = e.target.closest('.gal-img');
        if (!img) return;
        const idx = Number(img.dataset.index || 0);
        open(idx);
    });
}
