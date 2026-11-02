
import { useState } from 'react';
import { getPhotoUrl, setPhotoFavorite } from '../services/photoService';
import '../oblika/PhotoCard.css';

function PhotoCard({ photo, onDelete, onClick, onFavoriteChange }) {
    const [savingFavorite, setSavingFavorite] = useState(false);
    const [favoriteError, setFavoriteError] = useState('');

    const handleFavorite = async () => {
        if (savingFavorite) return;
        setSavingFavorite(true);
        setFavoriteError('');
        try {
            const updatedPhoto = await setPhotoFavorite(photo.id, !photo.favorite);
            onFavoriteChange?.(updatedPhoto);
        } catch (error) {
            setFavoriteError(error.message);
        } finally {
            setSavingFavorite(false);
        }
    };

    return (
        <div className="photo-card">
            <button
                type="button"
                className="photo-details-trigger"
                onClick={() => onClick(photo.id)}
                aria-label={`Podrobnosti fotografije ${photo.filename}`}
            >
                <img
                    src={getPhotoUrl(photo.id)}
                    alt={photo.filename}
                    loading="lazy"
                    decoding="async"
                    width="300"
                    height="300"
                />
            </button>

            <button
                type="button"
                className={`photo-favorite-button${photo.favorite ? ' is-favorite' : ''}`}
                onClick={handleFavorite}
                disabled={savingFavorite}
                aria-pressed={Boolean(photo.favorite)}
                aria-label={`${photo.favorite ? 'Odstrani iz priljubljenih' : 'Dodaj med priljubljene'}: ${photo.filename}`}
                title={photo.favorite ? 'Odstrani iz priljubljenih' : 'Dodaj med priljubljene'}
            >
                {photo.favorite ? '★' : '☆'}
            </button>
            {favoriteError && <p className="photo-favorite-error" role="alert">{favoriteError}</p>}

            {onDelete && (
                <button
                    type="button"
                    className="photo-delete-button"
                    onClick={() => onDelete(photo)}
                    aria-label={`Izbriši ${photo.filename}`}
                    title="Izbriši fotografijo"
                >
                    🗑
                </button>
            )}
        </div>
    );
}

export default PhotoCard;
