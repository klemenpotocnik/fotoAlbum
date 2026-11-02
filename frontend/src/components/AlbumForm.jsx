import { useState } from 'react';

function AlbumForm({ onSubmit, album = null }) {
    const [title, setTitle] = useState(album?.title ||'');
    const [description, setDescription] = useState(album?.description || '');
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    const isEditting = album !== null;

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        setError('');

        try {
            await onSubmit({ title, description });
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
            
            {error && <p className="error">{error}</p>}

            <button type="submit" disabled={saving}>
                {saving ? 'Shranjevanje...' : 'Shrani album'}
            </button>
        </form>
    );
}

export default AlbumForm;