(function () {
    let imgs = [];
    let idx = 0;

    const lb = document.getElementById('lightbox');
    const lbImg = lb.querySelector('.lb-img');
    const btnPrev = lb.querySelector('.lb-prev');
    const btnNext = lb.querySelector('.lb-next');
    const btnClose = lb.querySelector('.lb-close');

    function attach() {
        imgs = Array.from(document.querySelectorAll('#gallery img'));
        imgs.forEach((img, i) => {
            img.addEventListener('click', () => open(i));
        });
    }

    function open(i) {
        if (!imgs.length) return;
        idx = (i + imgs.length) % imgs.length;
        lbImg.src = imgs[idx].src;
        lb.classList.add('open');
        document.body.style.overflow = 'hidden';
    }

    function close() {
        lb.classList.remove('open');
        lbImg.src = '';
        document.body.style.overflow = '';
    }

    function next() { open(idx + 1); }
    function prev() { open(idx - 1); }

    // Клики по кнопкам
    btnNext.addEventListener('click', next);
    btnPrev.addEventListener('click', prev);
    btnClose.addEventListener('click', close);

    // Закрытие по клику на фон
    lb.addEventListener('click', (e) => {
        if (e.target === lb) close();
    });

    // Клавиатура
    document.addEventListener('keydown', (e) => {
        if (!lb.classList.contains('open')) return;
        if (e.key === 'Escape') close();
        if (e.key === 'ArrowRight') next();
        if (e.key === 'ArrowLeft') prev();
    });

    // Подхватываем первичную отрисовку и возможные перерисовки
    document.addEventListener('DOMContentLoaded', attach);
    document.addEventListener('gallery:rendered', attach);
})();
