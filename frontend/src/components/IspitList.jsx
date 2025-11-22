import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import ispitService from '../services/ispitService';
import PrijavaService from '../services/PrijavaService'; // 1. ISPRAVLJEN IMPORT
import { useAuth } from '../context/AuthContext';

const IspitList = () => {
    const [ispiti, setIspiti] = useState([]);
    const [prijavljeniIspiti, setPrijavljeniIspiti] = useState(new Set());
    const { user } = useAuth();

    useEffect(() => {
        ispitService.getAll()
            .then(response => {
                setIspiti(response.data);
            })
            .catch(error => {
                console.error("Greška (ispiti):", error);
            });

        if (user?.roles?.includes("STUDENT") && user.userId) {
            // 2. ISPRAVLJEN POZIV
            PrijavaService.getByStudent(user.userId)
                .then(response => {
                    const prijavljeniIdjevi = new Set(response.data.map(prijava => prijava.ispit.id));
                    setPrijavljeniIspiti(prijavljeniIdjevi);
                })
                .catch(error => console.error("Greška (prijave):", error));
        }
    }, [user]);

    const handleDelete = (id) => {
        if (window.confirm('Da li ste sigurni da želite da obrišete ovaj ispit?')) {
            ispitService.remove(id)
                .then(() => {
                    setIspiti(ispiti.filter(ispit => ispit.id !== id));
                })
                .catch(error => console.error("Greška prilikom brisanja ispita:", error));
        }
    };

    const handlePrijava = (ispitId) => {
        if (!user || !user.userId) {
            alert("Greška: Nije moguće identifikovati studenta. Molimo ulogujte se ponovo.");
            return;
        }

        // 3. ISPRAVLJEN POZIV
        PrijavaService.prijaviIspit(user.userId, ispitId)
            .then(() => {
                alert(`Ispit (ID: ${ispitId}) je uspešno prijavljen!`);
                setPrijavljeniIspiti(prevPrijavljeni => new Set(prevPrijavljeni).add(ispitId));
            })
            .catch(error => {
                const errorMessage = error.response?.data?.message || "Došlo je do greške prilikom prijave.";
                alert(errorMessage);
            });
    };

    return (
        <div className="container mt-4">
            <h1>Lista Ispita</h1>
            {user?.roles?.includes("ADMIN") && (
                <div style={{ marginBottom: '20px' }}>
                    <Link to="/ispiti/dodaj" className="btn btn-primary">Dodaj novi ispit</Link>
                </div>
            )}
            <table className="table table-striped">
                <thead>
                    <tr>
                        <th>Predmet</th>
                        <th>Profesor</th>
                        <th>Datum</th>
                        <th>Smer</th>
                        <th>Akcije</th>
                    </tr>
                </thead>
                <tbody>
                    {ispiti.map(ispit => (
                        <tr key={ispit.id}>
                            <td>{ispit.predmet}</td>
                            <td>{ispit.profesor}</td>
                            <td>{new Date(ispit.datum).toLocaleDateString()}</td>
                            <td>{ispit.smer}</td>
                            <td>
                                {user?.roles?.includes("ADMIN") && (
                                    <>
                                        <Link to={`/ispiti/izmeni/${ispit.id}`} className="btn btn-secondary btn-sm" style={{marginRight: '5px'}}>
                                            Izmeni
                                        </Link>
                                        <button onClick={() => handleDelete(ispit.id)} className="btn btn-danger btn-sm">
                                            Obriši
                                        </button>
                                    </>
                                )}
                                {user?.roles?.includes("STUDENT") && (
                                    prijavljeniIspiti.has(ispit.id) ? (
                                        <button className="btn btn-secondary btn-sm" disabled>Prijavljen</button>
                                    ) : (
                                        <button onClick={() => handlePrijava(ispit.id)} className="btn btn-success btn-sm">Prijavi</button>
                                    )
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default IspitList;