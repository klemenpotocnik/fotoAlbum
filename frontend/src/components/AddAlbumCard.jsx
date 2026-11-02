import '../oblika/AddAlbumCard.css';

function AddAlbumCard({ onClick }) {
    return (
        <button className="add-album-card" onClick={onClick}>
            <span className="add-album-icon">+</span>
            <span>Ustvari album</span>
        </button>
    );
}

export default AddAlbumCard;