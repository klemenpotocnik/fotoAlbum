import '../oblika/DeleteConfirmModal.css';

function DeleteConfirmModal({ album, onConfirm, onCancel, deleting, deleteAll = false }) {
    return (
        <div className="delete-overlay" onClick={onCancel}>
            <div
                className="delete-modal"
                role="dialog"
                aria-modal="true"
                aria-labelledby="delete-title"
                onClick={(event) => event.stopPropagation()}>

                <h2 id="delete-title">
                    {deleteAll ? 'Izbriši vse albume?' : 'Izbriši album?'}
                </h2>

                <p>
                    {deleteAll
                        ? 'Ali res želiš izbrisati vse albume?'
                        : <>Ali res želiš izbrisati album <strong>{album?.title}</strong>?</>
                    }
                </p>

                <p className="delete-warning">
                    Tega dejanja ni mogoče razveljaviti.
                </p>

                <div className="delete-actions">
                    <button
                        className="cancel-button"
                        onClick={onCancel}
                        disabled={deleting}
                    >
                        Prekliči
                    </button>

                    <button
                        className="delete-button"
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

export default DeleteConfirmModal;