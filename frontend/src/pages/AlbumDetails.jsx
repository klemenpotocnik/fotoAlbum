import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router';
import { getAlbumById } from '../services/albumService';
import { getPhotosByAlbumId, uploadPhotos, deletePhoto, deleteAllPhotos } from '../services/photoService';
import PhotoGallery from '../components/PhotoGallery';
import PhotoUploadModal from '../components/PhotoUploadModal';
import PhotoDeleteModal from '../components/PhotoDeleteModal';
import PhotoDetailsModal from '../components/PhotoDetailsModal';
import '../oblika/AlbumDetails.css';

function AlbumDetails() {
    const { id } = useParams();

    const [album, setAlbum] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const [photos, setPhotos] = useState([]);
    const [photosLoading, setPhotosLoading] = useState(true);
    const [photosError, setPhotosError] = useState('');

    const [isUploadModalOpen, setIsUploadModalOpen] = useState(false);

    const [photoToDelete, setPhotoToDelete] = useState(null);
    const [deletingPhoto, setDeletingPhoto] = useState(false);
    const [deleteError, setDeleteError] = useState('');

    const [isDeleteAllOpen, setIsDeleteAllOpen] = useState(false);
    const [deletingAll, setDeletingAll] = useState(false);
    const [deleteAllError, setDeleteAllError] = useState('');

    const [selectedPhotoId, setSelectedPhotoId] = useState(null);

    // Pridobivanje posameznega albuma
    useEffect(() => {
        let active = true;

        const loadAlbum = async () => {
            try {
                const data = await getAlbumById(id);

                if (active) {
                    setAlbum(data);
                    setError('');
                }
            } catch (error) {
                if (active) {
                    setError(error.message);
                }
            } finally {
                if (active) {
                    setLoading(false);
                }
            }
        };

        setLoading(true);
        setAlbum(null);
        setError('');
        loadAlbum();

        return () => {
            active = false;
        };
    }, [id]);

    // Pridobivanje fotografij izbranega albuma
    useEffect(() => {
        let active = true;

        const loadPhotos = async () => {
            try {
                const data = await getPhotosByAlbumId(id);

                if (active) {
                    setPhotos(data);
                    setPhotosError('');
                }
            } catch (error) {
                if (active) {
                    setPhotosError(error.message);
                }
            } finally {
                if (active) {
                    setPhotosLoading(false);
                }
            }
        };

        setPhotosLoading(true);
        setPhotos([]);
        setPhotosError('');
        loadPhotos();

        return () => {
            active = false;
        };
    }, [id]);

    if (loading) {
        return <p>Nalaganje albuma...</p>;
    }

    if (error) {
        return <p role="alert">{error}</p>;
    }

    if (!album) {
        return <p>Album ne obstaja.</p>;
    }

    const handleUploadPhotos = async (files) => {
        const uploadedPhotos = await uploadPhotos(id, files);

        setPhotos(prev => [...prev, ...uploadedPhotos]);
    };

    const handleDeletePhoto = async () => {
        if (!photoToDelete) return;

        setDeletingPhoto(true);
        setDeleteError('');

        try {
            await deletePhoto(photoToDelete.id);

            setPhotos(prev =>
                prev.filter(photo => photo.id !== photoToDelete.id)
            );

            setPhotoToDelete(null);
        } catch (error) {
            setDeleteError(error.message);
        } finally {
            setDeletingPhoto(false);
        }
    };

    const handleDeleteAllPhotos = async () => {
        setDeletingAll(true);
        setDeleteAllError('');

        try {
            await deleteAllPhotos(id);

            setPhotos([]);
            setIsDeleteAllOpen(false);
        } catch (error) {
            setDeleteAllError(error.message);

            try {
                const remainingPhotos = await getPhotosByAlbumId(id);
                setPhotos(remainingPhotos);
            } catch {
            }
        } finally {
            setDeletingAll(false);
        }
    };


    return (
        <main className="album-details">
            <Link className="back-link" to="/">
                ← Nazaj na albume
            </Link>


            <h1>{album.title}</h1>

            <p>{album.description}</p>

            <p>
                Datum ustvarjanja: {album.createdAt}
            </p>

            <div className="photos-header">
                <h2>Fotografije</h2>

                <div className="photos-actions">
                    <button
                        className="delete-all-photos-button"
                        onClick={() => setIsDeleteAllOpen(true)}
                        disabled={photos.length === 0}
                    >
                        Izbriši vse
                    </button>

                    <button onClick={() => setIsUploadModalOpen(true)}>
                        + Dodaj fotografije
                    </button>
                </div>
            </div>

            {photosLoading && <p>Nalaganje fotografij...</p>}

            {photosError && <p role="alert">{photosError}</p>}

            {!photosLoading && !photosError && (
                <PhotoGallery
                    photos={photos}
                    onFavoriteChange={updatedPhoto => setPhotos(previous =>
                        previous.map(photo => photo.id === updatedPhoto.id ? updatedPhoto : photo)
                    )}
                    onDelete={setPhotoToDelete}
                    onPhotoClick={setSelectedPhotoId}
                />
            )}

            {isUploadModalOpen && (
                <PhotoUploadModal
                    onUpload={handleUploadPhotos}
                    onClose={() => setIsUploadModalOpen(false)}
                />
            )}

            {photoToDelete && (
                <PhotoDeleteModal
                    photo={photoToDelete}
                    deleting={deletingPhoto}
                    error={deleteError}
                    onConfirm={handleDeletePhoto}
                    onCancel={() => {
                        setPhotoToDelete(null);
                        setDeleteError('');
                    }}
                />
            )}

            {isDeleteAllOpen && (
                <PhotoDeleteModal
                    deleteAll={true}
                    deleting={deletingAll}
                    error={deleteAllError}
                    onConfirm={handleDeleteAllPhotos}
                    onCancel={() => {
                        if (deletingAll) return;
                        setIsDeleteAllOpen(false);
                        setDeleteAllError('');
                    }}
                />
            )}

            {selectedPhotoId !== null && (
                <PhotoDetailsModal
                    photoId={selectedPhotoId}
                    onClose={() => setSelectedPhotoId(null)}
                />
            )}

        </main>
    );
}

export default AlbumDetails;
