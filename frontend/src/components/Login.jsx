import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import { useAuth } from '../context/AuthContext';

const Login = () => {
    const [brojIndeksa, setBrojIndeksa] = useState('');
    const [lozinka, setLozinka] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { login } = useAuth();

    const handleLogin = (e) => {
        e.preventDefault();
        setError(''); // Resetuj grešku pre novog pokušaja

        authService.login(brojIndeksa, lozinka)
            .then(data => {
                login(data.token); // Ažurira token u globalnom stanju
                navigate('/'); // Preusmeri na početnu zaštićenu stranicu
            })
            .catch(err => {
                console.error("Greška pri logovanju:", err);
                setError("Broj indeksa ili lozinka nisu ispravni.");
            });
    };

    return (
        <form onSubmit={handleLogin}>
            <h2>Prijava</h2>
            {error && <div className="alert alert-danger">{error}</div>}
            <div>
                <label>Broj Indeksa:</label>
                <input
                    type="text"
                    value={brojIndeksa}
                    onChange={(e) => setBrojIndeksa(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Lozinka:</label>
                <input
                    type="password"
                    value={lozinka}
                    onChange={(e) => setLozinka(e.target.value)}
                    required
                />
            </div>
            <button type="submit">Prijavi se</button>
        </form>
    );
};

export default Login;