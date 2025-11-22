import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import studentService from '../services/studentService';
import { useAuth } from '../context/AuthContext'; // 1. Uvozimo useAuth da bismo pristupili podacima o korisniku

const StudentList = () => {
    const [students, setStudents] = useState([]);
    const { user } = useAuth(); // 2. Preuzimamo 'user' objekat iz konteksta

    useEffect(() => {
        studentService.getStudents()
            .then(response => {
                setStudents(response.data);
            })
            .catch(error => {
                console.error("Došlo je do greške prilikom dobavljanja studenata:", error);
            });
    }, []);

    const handleDelete = (id) => {
        if (window.confirm('Da li ste sigurni da želite da obrišete ovog studenta?')) {
            studentService.deleteStudent(id)
                .then(() => {
                    setStudents(students.filter(student => student.id !== id));
                })
                .catch(error => console.error("Greška prilikom brisanja studenta:", error));
        }
    };

    return (
        <div className="container mt-4">
            <h1>Lista Studenata</h1>

            {/* 3. Prikazujemo "Dodaj studenta" dugme samo ako je korisnik ADMIN */}
            {user?.roles?.includes('ADMIN') && (
                <div style={{ marginBottom: '20px' }}>
                    <Link to="/studenti/dodaj" className="btn btn-primary">
                        Dodaj novog studenta
                    </Link>
                </div>
            )}

            <table className="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Ime</th>
                        <th>Prezime</th>
                        <th>Broj Indeksa</th>
                        <th>Smer</th>
                        {/* 4. Prikazujemo "Akcije" kolonu samo ako je korisnik ADMIN */}
                        {user?.roles?.includes('ADMIN') && <th>Akcije</th>}
                    </tr>
                </thead>
                <tbody>
                    {students.map(student => (
                        <tr key={student.id}>
                            <td>{student.id}</td>
                            <td>{student.ime}</td>
                            <td>{student.prezime}</td>
                            <td>{student.brojIndeksa}</td>
                            <td>{student.smer}</td>
                            {/* 5. Prikazujemo dugmiće za izmenu i brisanje samo ako je korisnik ADMIN */}
                            {user?.roles?.includes('ADMIN') && (
                                <td>
                                    <Link to={`/studenti/izmeni/${student.id}`} className="btn btn-secondary btn-sm" style={{marginRight: '5px'}}>
                                        Izmeni
                                    </Link>
                                    <button onClick={() => handleDelete(student.id)} className="btn btn-danger btn-sm">
                                        Obriši
                                    </button>
                                </td>
                            )}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default StudentList;