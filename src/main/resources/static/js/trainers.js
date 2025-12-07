// Если не загрузится фото — поставим заглушку (любой маленький png/jpg)
const FALLBACK_PHOTO = '/img/logo.png';

// данные о тренерах
const TRAINERS = [
    {
        name: 'Владислав Ригерт',
        //expYears: 20,
        photo: '/img/trainers/rigert.png',
        specs: [
            'Тяжёлая атлетика', 'Кроссфит', 'Пауэрлифтинг',
            'М/Ж тренинг и дети от 7 лет', 'Снижение/набор веса', 'Коррекция фигуры'
        ],
        about:
            'Тренерский стаж 20 лет. Мастер спорта России по тяжёлой атлетике. Дипломированный тренер.',
        schedule: 'Пн–Сб',
        price: '8 занятий — 5600; 12 занятий — 6000; Индивид. 1 мес. — 12000',
        phone: '+7 (928) 612-83-68'
    },
    {
        name: 'Александр Долгачев',
      //  expYears: 40,
        photo: '/img/trainers/dolgachev.png',
        specs: [
            'Тяжёлая атлетика', 'Пауэрлифтинг',
            'М/Ж тренинг и дети от 7 лет', 'Снижение/набор веса', 'Коррекция фигуры'
        ],
        about:
            'Тренерский стаж 40 лет. Мастер спорта России по тяжёлой атлетике. Дипломированный тренер.',
        schedule: 'Пн, Ср, Пт',
        price: '8 занятий — 4000; 12 занятий — 5000; Индивид. 1 мес. — 9000',
        phone: '+7 (952) 603-15-80'
    }
];

// экспортируем модуль в глобальную область
window.Trainers = {
    init() {
        const box = document.getElementById('trainerList');
        if (!box) return;

        const normalizePhone = (p) => p.replace(/[^\d+]/g, '');

        box.innerHTML = TRAINERS.map(t => `
      <article class="trainer">
        <div class="pic">
          <img src="${t.photo}" alt="${t.name}"
               onerror="this.onerror=null;this.src='${FALLBACK_PHOTO}'">
        </div>
        <div class="info">
         <h3>${t.name}</h3>


          <div class="tags">
            ${t.specs.map(s => `<span class="tag">${s}</span>`).join('')}
          </div>

          <p class="muted">${t.about}</p>
          <p class="muted"><b>График:</b> ${t.schedule}</p>
          <p class="muted"><b>Стоимость:</b> ${t.price}</p>
          <p class="muted"><b>Контакты:</b> <a href="tel:${normalizePhone(t.phone)}">${t.phone}</a></p>
        </div>
      </article>
    `).join('');
    }
};
