import { useEffect, useState } from 'react';
import { AuthContext } from './AuthContext';
import { clearSessionRequests } from '../services/api';
import * as auth from '../services/authService';

export default function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [retry, setRetry] = useState(0);

    useEffect(() => {
        let active = true;
        // Probe the session without treating an expected 401 as a network failure.
        fetch('/api/auth/me', { credentials: 'same-origin', cache: 'no-store' })
            .then(async response => {
                if (response.status === 401) return null;
                if (!response.ok) throw new Error('Seje ni mogoče preveriti. Preveri povezavo s strežnikom.');
                return response.json();
            })
            .then(value => { if (active) { setUser(value); setError(''); } })
            .catch(() => { if (active) setError('Seje ni mogoče preveriti. Preveri povezavo s strežnikom.'); })
            .finally(() => { if (active) setLoading(false); });
        return () => { active = false; };
    }, [retry]);

    useEffect(() => {
        const expire = () => { setUser(null); setError(''); };
        window.addEventListener('session-expired', expire);
        return () => window.removeEventListener('session-expired', expire);
    }, []);

    async function signIn(email, password) {
        clearSessionRequests();
        setUser(await auth.login(email, password));
    }
    async function signOut() {
        await auth.logout();
        clearSessionRequests();
        setUser(null);
    }
    function checkAgain() { setLoading(true); setError(''); setRetry(value => value + 1); }

    return <AuthContext.Provider value={{ user, loading, error, signIn, signOut, checkAgain }}>
        {children}
    </AuthContext.Provider>;
}
