import React from 'react';
// Sada nam BrowserRouter (kao Router) više ne treba ovde
import { Routes, Route, Link, Navigate, useNavigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';

import StudentList from './components/StudentList';
import StudentForm from './components/StudentForm';
import IspitList from './components/IspitList';
import IspitForm from './components/IspitForm';
import Login from './components/Login';
import Register from './components/Register';

const ProtectedRoute = ({ children, allowedRoles }) => {
    const { token, user } = useAuth();
    if (!token) {
        return <Navigate to="/login" replace />;
    }
    if (allowedRoles && !allowedRoles.some(role => user?.roles?.includes(role))) {
        return <Navigate to="/" replace />;
    }
    return children;
};

function App() {
  const { token, user, logout } = useAuth();
  const navigate = useNavigate(); // Sada ovo radi jer je <BrowserRouter> iznad App komponente

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    // UKLONILI SMO <Router> OMOTAČ ODAVDE
    <div>
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
          {/* ... sadržaj nav menija ostaje isti ... */}
          <div className="container-fluid">
            <Link className="navbar-brand" to="/">Studentska Služba</Link>
            <div className="navbar-nav me-auto">
              {user?.roles?.includes("ADMIN") && <Link className="nav-link" to="/studenti">Upravljanje Studentima</Link>}
              {token && <Link className="nav-link" to="/ispiti">Ispiti</Link>}
            </div>
            <div className="navbar-nav ms-auto">
              {!token ? (
                <>
                  <Link className="nav-link" to="/login">Prijava</Link>
                  <Link className="nav-link" to="/register">Registracija</Link>
                </>
              ) : (
                <button onClick={handleLogout} className="nav-link btn btn-link" style={{ textDecoration: 'none' }}>Odjavi se</button>
              )}
            </div>
          </div>
      </nav>

      <div className="container mt-3">
        <Routes>
          {/* ... sve rute ostaju iste ... */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<ProtectedRoute><Navigate to={user?.roles?.includes("ADMIN") ? "/studenti" : "/ispiti"} replace /></ProtectedRoute>} />
          <Route path="/studenti" element={<ProtectedRoute allowedRoles={['ADMIN']}><StudentList /></ProtectedRoute>} />
          <Route path="/studenti/dodaj" element={<ProtectedRoute allowedRoles={['ADMIN']}><StudentForm /></ProtectedRoute>} />
          <Route path="/studenti/izmeni/:id" element={<ProtectedRoute allowedRoles={['ADMIN']}><StudentForm /></ProtectedRoute>} />
          <Route path="/ispiti" element={<ProtectedRoute><IspitList /></ProtectedRoute>} />
          <Route path="/ispiti/dodaj" element={<ProtectedRoute allowedRoles={['ADMIN']}><IspitForm /></ProtectedRoute>} />
          <Route path="/ispiti/izmeni/:id" element={<ProtectedRoute allowedRoles={['ADMIN']}><IspitForm /></ProtectedRoute>} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;