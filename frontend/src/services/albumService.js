const API_URL = '/api/albums';

export async function getAlbums() {
    const response = await fetch(API_URL);

    if (!response.ok) {
        throw new Error('Napaka pri pridobivanju albumov');
    }

    return response.json();
}

export async function createAlbum(album) {
    const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(album),
    });

    if (!response.ok) {
        throw new Error('Napaka pri ustvarjanju albuma');
    }

    return response.json();
}

export async function updateAlbum(id, updatedAlbum) {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedAlbum),
    });

    if (!response.ok) {
        throw new Error('Napaka pri posodabljanju albuma');
    }

    return response.json();
}


export async function deleteAlbum(id) {
    const response = await fetch(`${API_URL}/${id}`, {
        method: 'DELETE'
    });

    if (!response.ok) {
        throw new Error('Napaka pri brisanju albuma');
    }
}

// Dodatna funkcija za pridobivanje albuma po ID-ju
export async function getAlbumById(id) {
    const response = await fetch(`${API_URL}/${id}`);

    if (!response.ok) {
        throw new Error('Napaka pri pridobivanju albuma');
    }

    return response.json();
}


export async function deleteAllAlbums() {
    const response = await fetch(`${API_URL}/all`, {
        method: 'DELETE'
    });

    if (!response.ok) {
        throw new Error('Napaka pri brisanju vseh albumov');
    }

    return response.text();
}