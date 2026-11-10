
import { useEffect, useState } from 'react';
import AlbumList from '../components/AlbumList';
import {
    getAlbums,
    createAlbum,
    updateAlbum,
    deleteAlbum,
    deleteAllAlbums
} from '../services/albumService';
import AlbumForm from '../components/AlbumForm';
import AddAlbumCard from '../components/AddAlbumCard';
import ForumModal from '../components/ForumModal';
import DeleteConfirmModal from '../components/DeleteConfirmModal';
import AllPhotos from '../components/AllPhotos';

function Home() {
    const [albums, setAlbums] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedAlbum, setSelectedAlbum] = useState(null);

    const [albumToDelete, setAlbumToDelete] = useState(null);
    const [deleting, setDeleting] = useState(false);

    const [isDeleteAllOpen, setIsDeleteAllOpen] = useState(false);

    const [photosRefreshKey, setPhotosRefreshKey] = useState(0);

    useEffect(() => {
        const loadAlbums = async () => {
            try {
                const data = await getAlbums();
                setAlbums(data);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        loadAlbums();
    }, []);

    const handleCreateAlbum = async (album) => {
        try {
            const newAlbum = await createAlbum(album);

            setAlbums(prevAlbums => [...prevAlbums, newAlbum]);

            setIsModalOpen(false);
        } catch (error) {
            setError(error.message);
            throw error;
        }
    };

    const handleUpdateAlbum = async (album) => {
        try {
            const updatedAlbum = await updateAlbum(
                selectedAlbum.id,
                album
            );

            setAlbums(prevAlbums =>
                prevAlbums.map(existingAlbum =>
                    existingAlbum.id === selectedAlbum.id
                        ? updatedAlbum
                        : existingAlbum
                )
            );

            setIsModalOpen(false);
            setSelectedAlbum(null);
        } catch (error) {
            setError(error.message);
            throw error;
        }
    };

    const handleOpenCreate = () => {
        setSelectedAlbum(null);
        setIsModalOpen(true);
    };

    const handleOpenEdit = (album) => {
        setSelectedAlbum(album);
        setIsModalOpen(true);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        setSelectedAlbum(null);
    };

    const handleDeleteAlbum = async () => {
        if (!albumToDelete) return;

        setDeleting(true);
        setError('');

        try {
            await deleteAlbum(albumToDelete.id);

            setAlbums(prevAlbums =>
                prevAlbums.filter(
                    album => album.id !== albumToDelete.id
                )
            );

            setPhotosRefreshKey(prev => prev + 1);
            setAlbumToDelete(null);
        } catch (error) {
            setError(error.message);
        } finally {
            setDeleting(false);
        }
    };

    const handleDeleteAllAlbums = async () => {
        setDeleting(true);
        setError('');

        try {
            await deleteAllAlbums();

            setAlbums([]);
            setPhotosRefreshKey(prev => prev + 1);
            setIsDeleteAllOpen(false);
        } catch (error) {
            setError(error.message);
        } finally {
            setDeleting(false);
        }
    };

    return (
        <main>
            <div className="albums-header">
                <h1>Moji albumi</h1>

                <button
                    className="delete-all-button"
                    onClick={() => setIsDeleteAllOpen(true)}
                    disabled={loading || deleting || albums.length === 0}
                >
                    Izbriši vse
                </button>
            </div>

            {loading && <p>Nalaganje albumov...</p>}

            {error && <p role="alert">{error}</p>}

            {!loading && !error && (
                <>
                    <AddAlbumCard onClick={handleOpenCreate} />

                    {albums.length === 0 ? (
                        <p className="no-albums-message">
                            Trenutno še nimaš nobenega albuma.
                        </p>
                    ) : (
                        <AlbumList
                            albums={albums}
                            onEdit={handleOpenEdit}
                            onDelete={setAlbumToDelete}
                        />
                    )}
                </>
            )}

            <div className="all-photos-section">
                <AllPhotos refreshKey={photosRefreshKey} />
            </div>

            {isModalOpen && (
                <ForumModal onClose={handleCloseModal}>
                    <AlbumForm
                        key={selectedAlbum?.id ?? 'new'}
                        album={selectedAlbum}
                        onSubmit={
                            selectedAlbum
                                ? handleUpdateAlbum
                                : handleCreateAlbum
                        }
                    />
                </ForumModal>
            )}

            {albumToDelete && (
                <DeleteConfirmModal
                    album={albumToDelete}
                    deleting={deleting}
                    onConfirm={handleDeleteAlbum}
                    onCancel={() => setAlbumToDelete(null)}
                />
            )}

            {isDeleteAllOpen && (
                <DeleteConfirmModal
                    deleteAll={true}
                    deleting={deleting}
                    onConfirm={handleDeleteAllAlbums}
                    onCancel={() => setIsDeleteAllOpen(false)}
                />
            )}
        </main>
    );
}

export default Home;
