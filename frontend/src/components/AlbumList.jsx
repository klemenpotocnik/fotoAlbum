import AlbumCard from './AlbumCard';

function AlbumList({ albums, onEdit, onDelete }) {
    return (
        <div className="album-list">
            {albums.map((album) => (
                <AlbumCard key={album.id} album={album} onEdit={onEdit} onDelete={onDelete} />
            ))}
        </div>
    );
}

export default AlbumList;