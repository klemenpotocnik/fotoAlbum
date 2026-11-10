import { useState } from 'react';
import { Link } from 'react-router';
import { useAuth } from '../auth/AuthContext';

export default function Navbar() {
    const { user, signOut } = useAuth();
    const [busy, setBusy] = useState(false);
    const [error, setError] = useState('');
    async function logout() {
        setBusy(true); setError('');
        try { await signOut(); }
        catch (err) { setError(err.message); }
        finally { setBusy(false); }
    }
    return <nav className="main-nav" aria-label="Glavna navigacija">
        <Link to={user ? '/' : '/login'} className="brand">Foto Album</Link>
        {user && <div className="nav-account"><span>{user.email}</span>
            <button onClick={logout} disabled={busy}>{busy ? 'Odjava …' : 'Odjava'}</button>
        </div>}
        {error && <p role="alert">{error}</p>}
    </nav>;
}
