import axios from 'axios';

const API_URL = 'http://localhost:8080/api/prijava';

const prijaviIspit = (studentId, ispitId) => {
    const prijavaData = {
        studentId: studentId,
        ispitId: ispitId
    };
    return axios.post(API_URL, prijavaData);
};

// DODATA JE OVA FUNKCIJA
const getByStudent = (studentId) => {
    return axios.get(`${API_URL}/student/${studentId}`);
};

// AŽURIRAN JE EXPORT
const prijavaService = {
    prijaviIspit,
    getByStudent
};

export default prijavaService;