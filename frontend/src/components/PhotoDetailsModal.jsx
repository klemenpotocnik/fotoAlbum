
import { useEffect, useState } from 'react';
import { getPhotoById, updatePhotoDetails } from '../services/photoService';
import PhotoDetailsForm from './photos/PhotoDetailsForm';
import '../oblika/PhotoDetailsModal.css';

function PhotoDetailsModal({ photoId, onClose, onPhotoChange }) {
    const [photo, setPhoto] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [editing, setEditing] = useState(false);
    const [saving, setSaving] = useState(false);
    const [saved, setSaved] = useState(false);

    async function saveDetails(details) {
        if (saving) return;
        setSaving(true);
        setError('');
        try {
            const updatedPhoto = await updatePhotoDetails(photoId, details);
            setPhoto(updatedPhoto);
            onPhotoChange?.(updatedPhoto);
            setEditing(false);
            setSaved(true);
        } catch (err) {
            setError(err.message);
        } finally {
            setSaving(false);
        }
    }

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

                    <button onClick={onClose} disabled={saving} aria-label="Zapri">
                        ×
                    </button>
                </div>

                {loading && <p>Nalaganje podatkov...</p>}

                {error && <p role="alert">{error}</p>}
                {saved && <p role="status">Podatki fotografije so shranjeni.</p>}

                {!loading && photo && (
                    <div className="photo-details-content">
                        {editing ? (
                            <PhotoDetailsForm photo={photo} saving={saving} onSave={saveDetails}
                                onCancel={() => { setEditing(false); setError(''); }} />
                        ) : (
                            <>
                                <p><strong>Naslov:</strong> {photo.title || photo.filename}</p>
                                <p className="photo-description"><strong>Opis:</strong> {photo.description || 'Ni opisa'}</p>
                                <button className="photo-details-edit" onClick={() => {
                                    setEditing(true); setSaved(false); setError('');
                                }}>Uredi podatke</button>
                            </>
                        )}
                        <p>
                            <strong>Ime datoteke:</strong> {photo.filename}
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
