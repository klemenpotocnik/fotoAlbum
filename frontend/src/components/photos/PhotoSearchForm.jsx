import { useId, useState } from 'react';
import '../../styles/components/photos/PhotoSearchForm.css';

function PhotoSearchForm({ onSearch }) {
    const id = useId();
    const [text, setText] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = event => {
        event.preventDefault();
        const query = text.trim();
        if (query.length > 100) {
            setError('Iskalni niz lahko vsebuje največ 100 znakov.');
            return;
        }
        setError('');
        onSearch(query);
    };

    const clearSearch = () => {
        setText('');
        setError('');
        onSearch('');
    };

    return (
        <form className="photo-search-form" onSubmit={handleSubmit} role="search">
            <label htmlFor={id}>Iskanje po naslovu ali opisu fotografije</label>
            <div className="photo-search-controls">
                <input id={id} type="search" value={text}
                    placeholder="Na primer morje" aria-invalid={Boolean(error)}
                    aria-describedby={error ? `${id}-error` : undefined}
                    onChange={event => setText(event.target.value)} />
                <button type="submit">Išči</button>
                <button type="button" onClick={clearSearch}>Počisti iskanje</button>
            </div>
            {error && <p id={`${id}-error`} role="alert">{error}</p>}
        </form>
    );
}

export default PhotoSearchForm;
