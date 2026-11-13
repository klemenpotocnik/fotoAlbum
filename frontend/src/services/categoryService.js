import { apiFetch } from './api';

export async function getCategories() {
    const response = await apiFetch('/api/categories');
    return response.json();
}
