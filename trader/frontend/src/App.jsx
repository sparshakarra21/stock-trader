import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Register from './components/Register';
import Dashboard from './components/Dashboard';
import './App.css';

function App() {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [username, setUsername] = useState(localStorage.getItem('username'));

  const handleAuth = (token, username) => {
    localStorage.setItem('token', token);
    localStorage.setItem('username', username);
    setToken(token);
    setUsername(username);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setToken(null);
    setUsername(null);
  };

  return (
    <Router>
      <div className="app">
        <Routes>
          <Route path="/login" element={token ? <Navigate to="/" /> : <Login onAuth={handleAuth} />} />
          <Route path="/register" element={token ? <Navigate to="/" /> : <Register onAuth={handleAuth} />} />
          <Route path="/" element={token ? <Dashboard username={username} onLogout={handleLogout} /> : <Navigate to="/login" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
