import { useNavigate } from 'react-router';

function AlbumCard({ album, onEdit, onDelete }) {
    const navigate = useNavigate();

    const handleOpenAlbum = () => {
        navigate(`/albums/${album.id}`);
    }

    return (
        <div className="album-card"
        onClick={handleOpenAlbum}>
            <h3>{album.title}</h3>
            <p>{album.description}</p>

            <button className="edit-button" onClick={(e) => { e.stopPropagation(); onEdit(album) }}>
                Uredi
            </button>

            <button className="delete-button" onClick={(e) => { e.stopPropagation(); onDelete(album) }}>
                Izbriši
            </button>
        </div>
    );
}

export default AlbumCard;