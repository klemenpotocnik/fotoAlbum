
import { useEffect, useState } from 'react';
import { getAllPhotos } from '../services/photoService';
import PhotoCard from './PhotoCard';
import PhotoDetailsModal from './PhotoDetailsModal';
import '../oblika/PhotoGallery.css';

function AllPhotos({ refreshKey }) {
    const [photos, setPhotos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [selectedPhotoId, setSelectedPhotoId] = useState(null);
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [filter, setFilter] = useState({});
    const [filterError, setFilterError] = useState('');
    const [favoritesOnly, setFavoritesOnly] = useState(false);
    const isFiltered = Boolean(filter.startDate);

    useEffect(() => {
        let active = true;

        const loadPhotos = async () => {
            setLoading(true);
            setError('');

            try {
                const data = await getAllPhotos({ ...filter, favoritesOnly });

                if (active) {
                    setPhotos(data);
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

        loadPhotos();

        return () => {
            active = false;
        };
    }, [refreshKey, filter, favoritesOnly]);

    const handleFilter = (event) => {
        event.preventDefault();
        if (!startDate || !endDate || startDate > endDate) {
            setFilterError('Izberi oba datuma. Začetni datum mora biti pred končnim ali enak.');
            return;
        }
        setFilterError('');
        setFilter({ startDate, endDate });
    };

    const clearFilter = () => {
        setStartDate('');
        setEndDate('');
        setFilterError('');
        setFilter({});
        setFavoritesOnly(false);
    };

    const handleFavoriteChange = (updatedPhoto) => {
        setPhotos(previous => previous
            .map(photo => photo.id === updatedPhoto.id ? updatedPhoto : photo)
            .filter(photo => !favoritesOnly || photo.favorite));
    };

    return (
        <section className="all-photos">
            <h2>{favoritesOnly ? 'Priljubljene fotografije' : 'Vse fotografije'} ({photos.length})</h2>

            <label className="photo-favorites-filter">
                <input type="checkbox" checked={favoritesOnly}
                    onChange={event => setFavoritesOnly(event.target.checked)} />
                Samo priljubljene fotografije
            </label>

            <form className="photo-date-filter" onSubmit={handleFilter}>
                <label>
                    Datum nastanka od
                    <input type="date" value={startDate} required
                        onChange={event => setStartDate(event.target.value)} />
                </label>
                <label>
                    Datum nastanka do
                    <input type="date" value={endDate} required min={startDate || undefined}
                        onChange={event => setEndDate(event.target.value)} />
                </label>
                <button type="submit">Filtriraj</button>
                <button type="button" className="photo-filter-clear" onClick={clearFilter}>
                    Počisti filter
                </button>
            </form>
            {filterError && <p role="alert">{filterError}</p>}
            {isFiltered && (
                <p>
                    Datum nastanka: {new Date(`${filter.startDate}T00:00:00`).toLocaleDateString('sl-SI')}
                    {' – '}{new Date(`${filter.endDate}T00:00:00`).toLocaleDateString('sl-SI')}
                </p>
            )}

            {loading && <p>Nalaganje fotografij...</p>}

            {error && <p role="alert">{error}</p>}

            {!loading && !error && photos.length === 0 && (
                <p>{isFiltered
                    ? (favoritesOnly ? 'V izbranem obdobju ni priljubljenih fotografij.' : 'V izbranem obdobju ni fotografij.')
                    : (favoritesOnly ? 'Trenutno še ni priljubljenih fotografij. Označi jih z zvezdico.' : 'Trenutno še ni nobene fotografije.')}</p>
            )}

            {!loading && !error && photos.length > 0 && (
                <div className="photo-gallery">
                    {photos.map(photo => (
                        <PhotoCard
                            key={photo.id}
                            photo={photo}
                            onClick={setSelectedPhotoId}
                            onFavoriteChange={handleFavoriteChange}
                        />
                    ))}
                </div>
            )}

            {selectedPhotoId !== null && (
                <PhotoDetailsModal
                    photoId={selectedPhotoId}
                    onClose={() => setSelectedPhotoId(null)}
                />
            )}
        </section>
    );
}

export default AllPhotos;
