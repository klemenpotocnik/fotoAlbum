
import '../oblika/PhotoDeleteModal.css';

function PhotoDeleteModal({
    photo,
    onConfirm,
    onCancel,
    deleting,
    error,
    deleteAll = false
}) {
    return (
        <div className="photo-delete-overlay">
            <div
                className="photo-delete-modal"
                role="dialog"
                aria-modal="true"
                aria-labelledby="photo-delete-title"
            >
                <h2 id="photo-delete-title">
                    {deleteAll ? 'Izbriši vse fotografije?' : 'Izbriši fotografijo?'}
                </h2>

                <p>
                    {deleteAll
                        ? 'Ali res želiš izbrisati vse fotografije iz tega albuma?'
                        : <>Ali res želiš izbrisati fotografijo <strong>{photo?.filename}</strong>?</>
                    }
                </p>

                <p>Dejanja ni mogoče razveljaviti.</p>

                {error && <p role="alert">{error}</p>}

                <div className="photo-delete-actions">
                    <button onClick={onCancel} disabled={deleting}>
                        Prekliči
                    </button>

                    <button
                        className="confirm-photo-delete"
                        onClick={onConfirm}
                        disabled={deleting}
                    >
                        {deleting ? 'Brisanje...' : 'Izbriši'}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default PhotoDeleteModal;
