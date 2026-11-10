import { apiFetch, clearCsrf } from './api';

export async function getCurrentUser() {
    return (await apiFetch('/api/auth/me', { notifyUnauthorized: false })).json();
}

export async function login(email, password) {
    await apiFetch('/api/auth/login', {
        method: 'POST', notifyUnauthorized: false,
        body: new URLSearchParams({ email: email.trim().toLowerCase(), password }),
    });
    clearCsrf();
    return getCurrentUser();
}

export async function signup(email, password) {
    return (await apiFetch('/api/auth/signup', {
        method: 'POST', notifyUnauthorized: false,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email.trim().toLowerCase(), password }),
    })).json();
}

export async function logout() {
    await apiFetch('/api/auth/logout', { method: 'POST' });
    clearCsrf();
}
