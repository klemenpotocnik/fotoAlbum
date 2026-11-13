export function categoryLabel(category) {
    return category?.name ?? 'Brez kategorije';
}

export function filterAlbums(albums, categoryId) {
    if (categoryId === 'all') return albums;
    return albums.filter(album => categoryId === 'uncategorized'
        ? album.category == null
        : String(album.category?.id) === String(categoryId));
}
