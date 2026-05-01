import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { login } from '../services/api';

function Login({ onAuth }) {
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await login(form);
      onAuth(res.data.token, res.data.username);
    } catch (err) {
      setError(err.response?.data?.error || 'Login failed');
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>Login</h2>
        {error && <p className="error" role="alert">{error}</p>}
        <form onSubmit={handleSubmit}>
          <label htmlFor="username">Username</label>
          <input id="username" type="text" placeholder="Username" value={form.username}
            onChange={(e) => setForm({ ...form, username: e.target.value })} required />
          <label htmlFor="password">Password</label>
          <input id="password" type="password" placeholder="Password" value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })} required />
          <button type="submit">Login</button>
        </form>
        <p>Don't have an account? <Link to="/register">Register</Link></p>
      </div>
    </div>
  );
}

export default Login;
