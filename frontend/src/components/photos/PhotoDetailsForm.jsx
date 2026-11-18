import { useState } from 'react';
import '../../styles/components/photos/PhotoDetailsForm.css';

export default function PhotoDetailsForm({ photo, saving, onSave, onCancel }) {
    const [title, setTitle] = useState(photo.title ?? '');
    const [description, setDescription] = useState(photo.description ?? '');
    const [submitted, setSubmitted] = useState(false);
    const titleInvalid = title.trim().length > 100;
    const descriptionInvalid = description.trim().length > 2000;

    function submit(event) {
        event.preventDefault();
        setSubmitted(true);
        if (saving || titleInvalid || descriptionInvalid) return;
        onSave({ title: title.trim(), description: description.trim() });
    }

    return (
        <form className="photo-details-form" onSubmit={submit}>
            <fieldset disabled={saving}>
                <label htmlFor="photo-title">Naslov fotografije</label>
                <input id="photo-title" value={title} onChange={event => setTitle(event.target.value)}
                    aria-invalid={submitted && titleInvalid} aria-describedby="photo-title-help" />
                <small id="photo-title-help">
                    {submitted && titleInvalid ? 'Naslov je predolg. ' : ''}Neobvezno, največ 100 znakov.
                </small>

                <label htmlFor="photo-description">Opis fotografije</label>
                <textarea id="photo-description" value={description} rows={5}
                    onChange={event => setDescription(event.target.value)}
                    aria-invalid={submitted && descriptionInvalid} aria-describedby="photo-description-help" />
                <small id="photo-description-help">
                    {submitted && descriptionInvalid ? 'Opis je predolg. ' : ''}Neobvezno, največ 2000 znakov.
                </small>

                <div className="photo-details-form-actions">
                    <button type="button" onClick={onCancel}>Prekliči</button>
                    <button type="submit">{saving ? 'Shranjevanje …' : 'Shrani'}</button>
                </div>
            </fieldset>
        </form>
    );
}
