import { useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router';
import { useAuth } from '../auth/AuthContext';
import { signup } from '../services/authService';
import '../oblika/Auth.css';

export default function AuthPage({ register = false }) {
    const { user, signIn } = useAuth();
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmation, setConfirmation] = useState('');
    const [error, setError] = useState('');
    const [busy, setBusy] = useState(false);
    if (user) return <Navigate to="/" replace />;

    async function submit(event) {
        event.preventDefault();
        setError('');
        if (register && password !== confirmation) { setError('Gesli se ne ujemata.'); return; }
        if (new TextEncoder().encode(password).length > 72) {
            setError('Geslo je predolgo.'); return;
        }
        setBusy(true);
        try {
            if (register) {
                await signup(email, password);
                setPassword(''); setConfirmation('');
                navigate('/login', { replace: true, state: { registered: true } });
            } else {
                await signIn(email, password);
                navigate('/', { replace: true });
            }
        } catch (err) {
            setError(err.message || 'Povezava s strežnikom ni uspela.');
        } finally { setBusy(false); }
    }

    return <main className="auth-page">
        <section className="auth-card" aria-labelledby="auth-title">
            <p className="auth-eyebrow">Tvoji spomini na enem mestu</p>
            <h1 id="auth-title">{register ? 'Ustvari račun' : 'Dobrodošel nazaj'}</h1>
            <p>{register ? 'Ustvari svojo zasebno zbirko albumov in fotografij.' : 'Prijavi se za dostop do svojih albumov in fotografij.'}</p>
            <form onSubmit={submit} autoComplete="off">
                <label htmlFor="email">E-pošta</label>
                <input id="email" type="email" autoComplete="off" required maxLength={254}
                    value={email} onChange={e => setEmail(e.target.value)} disabled={busy} />
                <label htmlFor="password">Geslo</label>
                <input id="password" type="password" autoComplete="off" data-lpignore="true" data-1p-ignore="true"
                    required minLength={register ? 8 : undefined} maxLength={72}
                    value={password} onChange={e => setPassword(e.target.value)} disabled={busy}
                    aria-describedby={register ? 'password-help' : undefined} />
                {register && <>
                    <small id="password-help">Najmanj 8 znakov.</small>
                    <label htmlFor="confirmation">Ponovi geslo</label>
                    <input id="confirmation" type="password" autoComplete="off" data-lpignore="true" data-1p-ignore="true" required
                        value={confirmation} onChange={e => setConfirmation(e.target.value)} disabled={busy} />
                </>}
                {error && <p className="auth-error" role="alert">{error}</p>}
                <button className="auth-submit" disabled={busy}>
                    {busy ? 'Počakaj …' : register ? 'Registracija' : 'Prijava'}
                </button>
            </form>
            <p className="auth-switch">{register ? 'Že imaš račun? ' : 'Še nimaš računa? '}
                <Link to={register ? '/login' : '/signup'}>{register ? 'Prijavi se' : 'Ustvari račun'}</Link>
            </p>
        </section>
    </main>;
}
