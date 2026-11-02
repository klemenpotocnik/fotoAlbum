
import { useEffect, useState } from 'react';
import { getPhotoById } from '../services/photoService';
import '../oblika/PhotoDetailsModal.css';

function PhotoDetailsModal({ photoId, onClose }) {
    const [photo, setPhoto] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        let active = true;

        const loadPhoto = async () => {
            try {
                const data = await getPhotoById(photoId);

                if (active) {
                    setPhoto(data);
                }
            } catch (err) {
                if (active) {
                    setError(err.message);
                }
            } finally {
                if (active) {
                    setLoading(false);
                }
            }
        };

        loadPhoto();

        return () => {
            active = false;
        };
    }, [photoId]);

    return (
        <div className="photo-details-overlay">
            <div className="photo-details-modal"
                role="dialog"
                aria-modal="true"
                aria-labelledby="photo-details-title"
            >
                <div className="photo-details-header">
                    <h2 id="photo-details-title">
                        Podrobnosti fotografije
                    </h2>

                    <button onClick={onClose} aria-label="Zapri">
                        ×
                    </button>
                </div>

                {loading && <p>Nalaganje podatkov...</p>}

                {error && <p role="alert">{error}</p>}

                {!loading && photo && (
                    <div className="photo-details-content">
                        <p>
                            <strong>Ime fotografije:</strong> {photo.filename}
                        </p>

                        <p>
                            <strong>Album:</strong> {photo.albumTitle}
                        </p>

                        <p>
                            <strong>Datum nastanka:</strong>{' '}
                            {photo.dateTaken
                                ? new Date(photo.dateTaken).toLocaleString('sl-SI')
                                : 'Ni podatka'}
                        </p>

                        <p>
                            <strong>Datum nalaganja:</strong>{' '}
                            {photo.uploadedAt
                                ? new Date(photo.uploadedAt).toLocaleString('sl-SI')
                                : 'Ni podatka'}
                        </p>
                    </div>
                )}
            </div>
        </div>
    );
}

export default PhotoDetailsModal;
