import '../oblika/DeleteAllButton.css';

function DeleteAllButton({ onClick, disabled }) {
    return (
        <button
            className="delete-all-button"
            onClick={onClick}
            disabled={disabled}
        >
            Izbriši vse
        </button>
    );
}

export default DeleteAllButton;