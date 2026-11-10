let csrf = null;
let generation = 0;
const activeRequests = new Set();

export function clearSessionRequests() {
    generation += 1;
    csrf = null;
    activeRequests.forEach(controller => controller.abort());
    activeRequests.clear();
}

export function clearCsrf() { csrf = null; }

async function csrfToken() {
    if (csrf) return csrf;
    const version = generation;
    const response = await fetch('/api/auth/csrf', { credentials: 'same-origin', cache: 'no-store' });
    if (!response.ok) throw new Error('Varnostnega žetona ni mogoče pridobiti. Poskusi znova.');
    const token = await response.json();
    if (version !== generation) throw new DOMException('Seja se je spremenila.', 'AbortError');
    csrf = token;
    return token;
}

function sessionExpired() {
    clearSessionRequests();
    window.dispatchEvent(new Event('session-expired'));
}

export async function apiFetch(url, options = {}) {
    const { notifyUnauthorized = true, ...request } = options;
    const version = generation;
    const controller = new AbortController();
    activeRequests.add(controller);
    try {
        const headers = new Headers(request.headers);
        if (!['GET', 'HEAD', 'OPTIONS'].includes((request.method || 'GET').toUpperCase())) {
            const token = await csrfToken();
            headers.set(token.headerName, token.token);
        }
        const response = await fetch(url, {
            ...request, headers, signal: controller.signal,
            credentials: 'same-origin', cache: 'no-store',
        });
        if (version !== generation) throw new DOMException('Seja se je spremenila.', 'AbortError');
        if (!response.ok) {
            if (response.status === 401 && notifyUnauthorized) sessionExpired();
            if (response.status === 403) {
                csrf = null;
                // A lost session can first appear as a CSRF failure on a write.
                if (notifyUnauthorized) {
                    const me = await fetch('/api/auth/me', { credentials: 'same-origin', cache: 'no-store' });
                    if (version !== generation) throw new DOMException('Seja se je spremenila.', 'AbortError');
                    if (me.status === 401) sessionExpired();
                }
            }
            const error = await response.json().catch(() => ({}));
            throw new Error(error.message || error.detail || 'Zahteve ni mogoče izvesti. Poskusi znova.');
        }
        return response;
    } finally {
        activeRequests.delete(controller);
    }
}
