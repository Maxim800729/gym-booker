async function showMy(page = 0) {
    const emailInput = document.getElementById('my-email');
    const box = document.getElementById('my-bookings');
    if (!emailInput || !box) return;

    const email = emailInput.value.trim();
    if (!email) { alert('Укажите email'); return; }

    const size = 10;
    const url = `/api/bookings?q=${encodeURIComponent(email)}&page=${page}&size=${size}&sort=createdAt,desc`;

    try {
        const data = await fetchJSON(url);
        if (!data.content.length) {
            box.innerHTML = `<div class="text-muted">Записей не найдено</div>`;
            return;
        }

        box.innerHTML = data.content.map(b => `
      <div class="card mb-2">
        <div class="card-body">
          <b>Запись #${b.id}</b><br/>
          <div>Статус: <b>${b.status}</b></div>
          <div>Создана: ${new Date(b.createdAt).toLocaleString()}</div>
          ${b.status === 'ACTIVE'
            ? `<button class="btn btn-sm btn-outline-danger mt-2" data-cancel="${b.id}">Отменить</button>`
            : ''
        }
        </div>
      </div>
    `).join('');

        // Пагинация
        if (data.totalPages > 1) {
            const pager = document.createElement('div');
            pager.className = 'mt-2';
            pager.innerHTML = `
        <button class="btn btn-sm btn-secondary" ${data.first ? 'disabled' : ''} data-page="${data.number - 1}">← Назад</button>
        <span class="mx-2">${data.number + 1} / ${data.totalPages}</span>
        <button class="btn btn-sm btn-secondary" ${data.last ? 'disabled' : ''} data-page="${data.number + 1}">Вперед →</button>
      `;
            box.appendChild(pager);

            pager.addEventListener('click', (e) => {
                const btn = e.target.closest('button[data-page]');
                if (btn && !btn.disabled) showMy(+btn.dataset.page);
            });
        }

        // Делегирование «Отменить»
        box.addEventListener('click', async (e) => {
            const btn = e.target.closest('button[data-cancel]');
            if (!btn) return;
            const id = +btn.dataset.cancel;
            if (!confirm('Отменить запись?')) return;
            try {
                await fetchJSON(`/api/bookings/${id}/status/CANCELLED`, { method: 'PATCH' });
                showMy();
            } catch (err) {
                alert('Не удалось отменить: ' + err.message);
            }
        }, { once: true });

    } catch (err) {
        alert('Ошибка загрузки: ' + err.message);
    }
}

window.Bookings = {
    init() {
        const btn = document.getElementById('my-show-btn');
        if (btn) btn.addEventListener('click', () => showMy(0));
    }
};
document.addEventListener('DOMContentLoaded', () => {
    // Элементы блока "Мои записи"
    const emailInput = document.querySelector('#profile-email-input');
    const showBtn = document.querySelector('#profile-show-btn');
    const infoBox = document.querySelector('#profile-info');
    const bookingsList = document.querySelector('#profile-bookings');

    // Если на странице нет этих элементов — выходим (чтобы скрипт не падал)
    if (!emailInput || !showBtn || !infoBox || !bookingsList) {
        return;
    }

    // Подставляем сохранённый email, если есть
    const savedEmail = localStorage.getItem('rw_profile_email');
    if (savedEmail && !emailInput.value) {
        emailInput.value = savedEmail;
    }

    // Обработчик кнопки "Показать"
    showBtn.addEventListener('click', async () => {
        const email = emailInput.value.trim();

        if (!email) {
            alert('Введите email');
            return;
        }

        showBtn.disabled = true;
        const oldText = showBtn.textContent;
        showBtn.textContent = 'Загружаем...';

        try {
            const profile = await apiGet(
                `/api/clients/profile/by-email?email=${encodeURIComponent(email)}`
            );

            // Сохраняем email в localStorage
            localStorage.setItem('rw_profile_email', email);

            // Отрисовываем блок с информацией о клиенте
            infoBox.innerHTML = `
                <h3>${profile.fullName}</h3>
                <p><b>Email:</b> ${profile.email}</p>
                <p><b>Телефон:</b> ${profile.phone || '—'}</p>
                <p><b>Адрес:</b> ${profile.address || '—'}</p>
                <p><b>Дата рождения:</b> ${profile.birthDate || '—'}</p>
                <p><b>Клиент с:</b> ${profile.createdAt || '—'}</p>
            `;

            // Список бронирований
            bookingsList.innerHTML = '';

            if (!profile.bookings || profile.bookings.length === 0) {
                bookingsList.innerHTML = '<p>Записей пока нет.</p>';
            } else {
                profile.bookings.forEach(b => {
                    const item = document.createElement('li');
                    item.className = 'profile-booking-item';
                    item.innerHTML = `
                        <div><b>${b.className}</b></div>
                        <div>Зал: ${b.roomName}</div>
                        <div>Длительность: ${b.durationMinutes} мин</div>
                        <div>Статус: ${b.status}</div>
                        <div>Оплата: ${b.paid ? 'оплачено' : 'не оплачено'}</div>
                    `;
                    bookingsList.appendChild(item);
                });
            }
        } catch (e) {
            console.error(e);
            alert('Не удалось загрузить профиль. Проверь email и попробуй ещё раз.');
        } finally {
            showBtn.disabled = false;
            showBtn.textContent = oldText;
        }
    });
});
