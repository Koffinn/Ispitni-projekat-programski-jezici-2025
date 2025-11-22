import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/';

const register = (studentData) => {
    return axios.post(API_URL + 'register', studentData);
};

const login = (brojIndeksa, lozinka) => {
    return axios.post(API_URL + 'login', { brojIndeksa, lozinka })
        .then(response => {
            if (response.data.token) {
                localStorage.setItem('token', response.data.token);
            }
            return response.data;
        });
};

const logout = () => {
    localStorage.removeItem('token');
};

const authService = { register, login, logout };
export default authService;