import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';

const Register = () => {
    const [student, setStudent] = useState({
        ime: '',
        prezime: '',
        brojIndeksa: '',
        smer: '',
        lozinka: ''
    });
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setStudent(prevState => ({ ...prevState, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        authService.register(student)
            .then(() => {
                alert("Registracija uspešna! Sada se možete prijaviti.");
                navigate('/login');
            })
            .catch(error => {
                console.error("Greška prilikom registracije:", error);
                alert("Došlo je do greške prilikom registracije.");
            });
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Registracija novog studenta</h2>
            <div>
                <label>Ime:</label>
                <input type="text" name="ime" value={student.ime} onChange={handleChange} required />
            </div>
            <div>
                <label>Prezime:</label>
                <input type="text" name="prezime" value={student.prezime} onChange={handleChange} required />
            </div>
            <div>
                <label>Broj Indeksa:</label>
                <input type="text" name="brojIndeksa" value={student.brojIndeksa} onChange={handleChange} required />
            </div>
            <div>
                <label>Smer:</label>
                <input type="text" name="smer" value={student.smer} onChange={handleChange} required />
            </div>
            <div>
                <label>Lozinka:</label>
                <input type="password" name="lozinka" value={student.lozinka} onChange={handleChange} required />
            </div>
            <button type="submit">Registruj se</button>
        </form>
    );
};

export default Register;