const API_URL = '/api';

export async function getPhotosByAlbumId(albumId) {
    const response = await fetch(
        `${API_URL}/albums/${albumId}/photos`
    );

    if (!response.ok) {
        throw new Error('Napaka pri pridobivanju fotografij');
    }

    return response.json();
}

export function getPhotoUrl(photoId) {
    return `${API_URL}/photos/${photoId}/file`;
}


export async function uploadPhotos(albumId, files) {
    const formData = new FormData();

    for (const file of files) {
        formData.append('files', file);
    }

    const response = await fetch(`/api/albums/${albumId}/photos`, {
        method: 'POST',
        body: formData
    });

    if (!response.ok) {
        throw new Error('Napaka pri nalaganju fotografij');
    }

    return response.json();
}

export async function deletePhoto(id) {
    const response = await fetch(`/api/photos/${id}`, {
        method: 'DELETE'
    });

    if (!response.ok) {
        throw new Error('Napaka pri brisanju fotografije');
    }
}

export async function deleteAllPhotos(albumId) {
    const response = await fetch(`/api/albums/${albumId}/photos`, {
        method: 'DELETE'
    });

    if (!response.ok) {
        throw new Error('Napaka pri brisanju vseh fotografij');
    }
}

export async function getPhotoById(id) {
    const response = await fetch(`/api/photos/${id}`);

    if (!response.ok) {
        throw new Error('Napaka pri pridobivanju podatkov fotografije');
    }

    return response.json();
}

export async function getAllPhotos({ startDate, endDate, favoritesOnly } = {}) {
    const params = new URLSearchParams();
    if (startDate) params.set('startDate', startDate);
    if (endDate) params.set('endDate', endDate);
    if (favoritesOnly) params.set('favoritesOnly', 'true');

    const response = await fetch(`${API_URL}/photos${params.size ? `?${params}` : ''}`);

    if (!response.ok) {
        throw new Error('Napaka pri pridobivanju fotografij');
    }

    return response.json();
}

export async function setPhotoFavorite(id, favorite) {
    const response = await fetch(`${API_URL}/photos/${id}/favorite`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ favorite })
    });
    if (!response.ok) {
        throw new Error('Napaka pri shranjevanju priljubljene fotografije');
    }
    return response.json();
}
