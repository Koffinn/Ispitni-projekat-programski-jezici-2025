// Puna putanja: src/components/StudentForm.jsx

import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import studentService from '../services/studentService';

const StudentForm = () => {
    const [student, setStudent] = useState({ ime: '', prezime: '', brojIndeksa: '', smer: '', lozinka: '' });
    const navigate = useNavigate();
    const { id } = useParams();

    useEffect(() => {
        if (id) {
            studentService.getStudent(id)
                .then(response => {
                    setStudent(response.data);
                })
                .catch(error => console.error("Greška prilikom učitavanja studenta:", error));
        }
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setStudent(prevState => ({ ...prevState, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (id) {
            studentService.updateStudent(id, student)
                .then(() => navigate('/studenti'))
                .catch(error => console.error("Update error:", error));
        } else {
            studentService.createStudent(student)
                .then(() => navigate('/studenti'))
                .catch(error => console.error("Create error:", error));
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>{id ? 'Izmeni studenta' : 'Dodaj novog studenta'}</h2>
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
                <input type="password" name="lozinka" value={student.lozinka} onChange={handleChange} placeholder={id ? "Ostavite prazno da se ne menja" : ""} required={!id} />
            </div>
            <button type="submit">Sačuvaj</button>
        </form>
    );
};

export default StudentForm;