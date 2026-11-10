import { BrowserRouter, Routes, Route, Navigate, Outlet, useLocation } from 'react-router';
import './App.css';
import AuthProvider from './auth/AuthProvider';
import { useAuth } from './auth/AuthContext';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import AlbumDetails from './pages/AlbumDetails';
import AuthPage from './pages/AuthPage';

function ProtectedRoute() {
    const { user } = useAuth();
    return user ? <Outlet key={user.id} /> : <Navigate to="/login" replace />;
}

function AppContent() {
    const { user, loading, error, checkAgain } = useAuth();
    const location = useLocation();
    if (loading) return <main className="auth-status" role="status">Preverjanje prijave …</main>;
    if (error) return <main className="auth-status"><p role="alert">{error}</p><button onClick={checkAgain}>Poskusi znova</button></main>;
    return <>
        <Navbar />
        {location.pathname === '/login' && location.state?.registered && !user &&
            <p className="auth-success" role="status">Račun je ustvarjen. Sedaj se lahko prijaviš.</p>}
        <Routes key={user?.id ?? 'guest'}>
            <Route path="/login" element={<AuthPage key="login" />} />
            <Route path="/signup" element={<AuthPage key="signup" register />} />
            <Route element={<ProtectedRoute />}>
                <Route path="/" element={<Home />} />
                <Route path="/albums/:id" element={<AlbumDetails />} />
            </Route>
            <Route path="*" element={<Navigate to={user ? '/' : '/login'} replace />} />
        </Routes>
    </>;
}

export default function App() {
    return <AuthProvider><BrowserRouter><AppContent /></BrowserRouter></AuthProvider>;
}
