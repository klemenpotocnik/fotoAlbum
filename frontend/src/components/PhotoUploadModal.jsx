
import { useEffect, useState } from 'react';
import '../oblika/PhotoUploadModal.css';

function PhotoUploadModal({ onUpload, onClose }) {
    const [files, setFiles] = useState([]);
    const [previews, setPreviews] = useState([]);
    const [uploading, setUploading] = useState(false);
    const [error, setError] = useState('');

    // Ustvarjanje predogledov fotografij
    useEffect(() => {
        const urls = files.map(file => ({
            file,
            url: URL.createObjectURL(file)
        }));

        setPreviews(urls);

        return () => {
            urls.forEach(item => URL.revokeObjectURL(item.url));
        };
    }, [files]);

    const handleFileChange = (event) => {
        const selectedFiles = Array.from(event.target.files);

        // Preverimo tip in velikost posamezne datoteke
        const validFiles = selectedFiles.filter(file =>
            ['image/jpeg', 'image/png'].includes(file.type) &&
            file.size <= 10 * 1024 * 1024
        );

        if (validFiles.length !== selectedFiles.length) {
            setError('Dovoljene so samo JPG/PNG slike do 10 MB.');
        } else {
            setError('');
        }

        setFiles(prev => [...prev, ...validFiles]);
        event.target.value = '';
    };

    const removeFile = (index) => {
        setFiles(prev => prev.filter((_, i) => i !== index));
    };

    const handleUpload = async () => {
        if (files.length === 0) return;

        setUploading(true);
        setError('');

        try {
            await onUpload(files);
            onClose();
        } catch (err) {
            setError(err.message);
        } finally {
            setUploading(false);
        }
    };

    return (
        <div className="photo-upload-overlay">
            <div
                className="photo-upload-modal"
                role="dialog"
                aria-modal="true"
                aria-label="Dodaj fotografije"
            >
                <div className="photo-upload-header">
                    <h2>Dodaj fotografije</h2>

                    <button
                        className="photo-upload-close"
                        onClick={onClose}
                        disabled={uploading}
                        aria-label="Zapri"
                    >
                        ×
                    </button>
                </div>

                <label className="photo-upload-select">
                    <span>+ Izberi fotografije</span>
                    <small>JPG ali PNG, največ 10 MB na sliko</small>

                    <input
                        type="file"
                        accept="image/jpeg,image/png"
                        multiple
                        onChange={handleFileChange}
                        disabled={uploading}
                    />
                </label>

                {previews.length > 0 && (
                    <>
                        <p>Izbrane fotografije ({files.length})</p>

                        <div className="photo-preview-grid">
                            {previews.map((item, index) => (
                                <div className="photo-preview" key={item.url}>
                                    <img
                                        src={item.url}
                                        alt={item.file.name}
                                    />

                                    <button
                                        onClick={() => removeFile(index)}
                                        disabled={uploading}
                                        aria-label="Odstrani fotografijo"
                                    >
                                        ×
                                    </button>
                                </div>
                            ))}
                        </div>
                    </>
                )}

                {error && <p className="photo-upload-error">{error}</p>}

                <div className="photo-upload-actions">
                    <button
                        onClick={onClose}
                        disabled={uploading}
                    >
                        Prekliči
                    </button>

                    <button
                        onClick={handleUpload}
                        disabled={files.length === 0 || uploading}
                    >
                        {uploading
                            ? 'Nalaganje...'
                            : `Naloži fotografije (${files.length})`
                        }
                    </button>
                </div>
            </div>
        </div>
    );
}

export default PhotoUploadModal;
