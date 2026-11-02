import PhotoCard from './PhotoCard';
import '../oblika/PhotoGallery.css';

function PhotoGallery({ photos, onDelete, onPhotoClick, onFavoriteChange }) {
    if (photos.length === 0) {
        return <p>Ta album še nima fotografij.</p>;
    }

    return (
        <div className="photo-gallery">
            {photos.map(photo => (
                <PhotoCard
                    key={photo.id}
                    photo={photo}
                    onDelete={onDelete}
                    onClick={onPhotoClick}
                    onFavoriteChange={onFavoriteChange}
                />
            ))}
        </div>
    );
}

export default PhotoGallery;
