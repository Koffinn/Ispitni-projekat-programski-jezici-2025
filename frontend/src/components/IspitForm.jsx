import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import ispitService from '../services/ispitService'; //

const IspitForm = () => {
    const [ispit, setIspit] = useState({
        predmet: '',
        profesor: '',
        datum: '',
        smer: ''
    });
    const navigate = useNavigate();
    const { id } = useParams();

    useEffect(() => {
        if (id) {
            ispitService.get(id)
                .then(response => {
                    const formattedDate = response.data.datum ? response.data.datum.split('T')[0] : '';
                    setIspit({ ...response.data, datum: formattedDate });
                })
                .catch(error => console.error("Greška prilikom učitavanja ispita:", error));
        }
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setIspit(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // handleSubmit сада може исправно да користи 'ispitService'
        const promise = id
            ? ispitService.update(id, ispit)
            : ispitService.create(ispit);

        promise
            .then(() => {
                navigate('/ispiti');
            })
            .catch(error => {
                console.error("Došlo je do greške prilikom čuvanja ispita:", error);
            });
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>{id ? 'Izmeni ispit' : 'Dodaj novi ispit'}</h2>
            <div>
                <label>Predmet:</label>
                <input type="text" name="predmet" value={ispit.predmet} onChange={handleChange} required />
            </div>
            <div>
                <label>Profesor:</label>
                <input type="text" name="profesor" value={ispit.profesor} onChange={handleChange} required />
            </div>
            <div>
                <label>Datum:</label>
                <input type="date" name="datum" value={ispit.datum} onChange={handleChange} required />
            </div>
            <div>
                <label>Smer:</label>
                <input type="text" name="smer" value={ispit.smer} onChange={handleChange} required />
            </div>
            <button type="submit">Sačuvaj</button>
        </form>
    );
};

export default IspitForm;