import axios from 'axios';

const API_URL = 'http://localhost:8080/api/student'; // Osnovna adresa za studente

const getStudents = () => {
    return axios.get(API_URL);
};

const getStudent = (id) => {
    return axios.get(`${API_URL}/${id}`);
};

const createStudent = (studentData) => {
    return axios.post(API_URL, studentData);
};

const updateStudent = (id, studentData) => {
    return axios.put(`${API_URL}/${id}`, studentData);
};

const deleteStudent = (id) => {
    return axios.delete(`${API_URL}/${id}`);
};

// Objedinimo sve funkcije u jedan objekat
const studentService = {
    getStudents,
    getStudent,
    createStudent,
    updateStudent,
    deleteStudent
};

export default studentService;