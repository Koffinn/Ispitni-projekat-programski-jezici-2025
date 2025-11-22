import React, { createContext, useState, useContext } from 'react';
import { jwtDecode } from 'jwt-decode'; // Proverite da li ste instalirali: npm install jwt-decode

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    // Funkcija koja proverava token pri prvom učitavanju aplikacije
    const getInitialToken = () => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                // Proveravamo da li je token istekao
                const decoded = jwtDecode(token);
                if (decoded.exp * 1000 < Date.now()) {
                    localStorage.removeItem('token');
                    return null;
                }
                return token;
            } catch (e) {
                localStorage.removeItem('token');
                return null;
            }
        }
        return null;
    };

    const [token, setToken] = useState(getInitialToken());

    const login = (newToken) => {
        localStorage.setItem('token', newToken);
        setToken(newToken);
    };

    const logout = () => {
        localStorage.removeItem('token');
        setToken(null);
    };

    // Vrednosti koje će biti dostupne celoj aplikaciji
    const value = {
        token,
        user: token ? jwtDecode(token) : null, // Dekodiramo token da dobijemo podatke o korisniku
        login,
        logout
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};

// KLJUČNI DEO KOJI VAM JE NEDOSTAJAO ILI BIO POGREŠAN
// Ovo je custom hook koji omogućava lak pristup kontekstu
export const useAuth = () => {
    return useContext(AuthContext);
};