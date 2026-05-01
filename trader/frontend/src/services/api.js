import axios from 'axios';

const API = axios.create({ baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api' });

API.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export const register = (data) => API.post('/auth/register', data);
export const login = (data) => API.post('/auth/login', data);
export const getStocks = () => API.get('/stocks');
export const buyStock = (data) => API.post('/stocks/buy', data);
export const sellStock = (data) => API.post('/stocks/sell', data);
export const getPortfolio = () => API.get('/portfolio');
export const getTransactions = () => API.get('/portfolio/transactions');
