// Базовый URL API (если фронт и бэк на одном домене — можно оставить пустым)
const API_BASE = '';

/** Универсальный JSON-fetch с обработкой ошибок */
async function fetchJSON(url, options = {}) {
    const res = await fetch(API_BASE + url, {
        headers: { 'Accept': 'application/json', ...(options.headers || {}) },
        ...options,
    });
    if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new Error(`HTTP ${res.status}: ${text || res.statusText}`);
    }
    // 204 No Content
    if (res.status === 204) return null;
    return res.json();
}
