import { useState } from 'react';

function AlbumForm({ onSubmit, album = null, categories = [] }) {
    const [title, setTitle] = useState(album?.title ||'');
    const [description, setDescription] = useState(album?.description || '');
    const [categoryId, setCategoryId] = useState(album?.category?.id ?? '');
    const [customCategoryName, setCustomCategoryName] = useState('');
    const isOther = categories.find(category => String(category.id) === String(categoryId))?.name === 'Drugo';
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    const isEditting = album !== null;

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        setError('');

        try {
            await onSubmit({ title, description, categoryId: categoryId === '' ? null : Number(categoryId),
                customCategoryName: isOther ? customCategoryName.trim() : null });
        } catch (error) {
            setError(error.message);
        } finally {
            setSaving(false);
        }
    };

    return (
        <form className="album-form" onSubmit={handleSubmit}>
            <h2>{isEditting ? 'Uredi album' : 'Dodaj nov album'}</h2>

            <input
                type="text"
                placeholder="Naslov albuma"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                required
            />
            <textarea
                placeholder="Opis albuma"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
            />
            <label htmlFor="album-category">Kategorija albuma</label>
            <select id="album-category" value={categoryId} onChange={e => setCategoryId(e.target.value)} disabled={saving}>
                <option value="">Brez kategorije</option>
                {categories.map(category => (
                    <option key={category.id} value={category.id}>{category.name}</option>
                ))}
            </select>

            {isOther && (
                <>
                    <label htmlFor="custom-category-name">Vpiši svojo kategorijo</label>
                    <input id="custom-category-name" type="text" value={customCategoryName}
                        onChange={event => setCustomCategoryName(event.target.value)}
                        placeholder="Na primer Hišni ljubljenčki" maxLength={100} required disabled={saving} />
                    <p>Kategorija bo na voljo tudi pri drugih tvojih albumih.</p>
                </>
            )}

            {error && <p className="error">{error}</p>}

            <button type="submit" disabled={saving || (isOther && !customCategoryName.trim())}>
                {saving ? 'Shranjevanje...' : 'Shrani album'}
            </button>
        </form>
    );
}

export default AlbumForm;
