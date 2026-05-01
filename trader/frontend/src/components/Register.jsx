import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { register } from '../services/api';

function Register({ onAuth }) {
  const [form, setForm] = useState({ username: '', email: '', password: '' });
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await register(form);
      onAuth(res.data.token, res.data.username);
    } catch (err) {
      setError(err.response?.data?.error || 'Registration failed');
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Register</h2>
        {error && <p className="error" role="alert">{error}</p>}
        <form onSubmit={handleSubmit}>
          <label htmlFor="username">Username</label>
          <input id="username" type="text" placeholder="Username" value={form.username}
            onChange={(e) => setForm({ ...form, username: e.target.value })} required />
          <label htmlFor="email">Email</label>
          <input id="email" type="email" placeholder="Email" value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })} required />
          <label htmlFor="password">Password</label>
          <input id="password" type="password" placeholder="Password" value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })} required />
          <button type="submit">Register</button>
        </form>
        <p>Already have an account? <Link to="/login">Login</Link></p>
      </div>
    </div>
  );
}

export default Register;
