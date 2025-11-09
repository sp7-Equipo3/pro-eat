export const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || 'https://pro-eat.onrender.com';

export const API_TIMEOUT = 30000;

export const DEFAULT_HEADERS = {
  'Content-Type': 'application/json',
  Accept: 'application/json'
};

export const getAuthToken = () => {
  return localStorage.getItem('token') || null;
};

export const setAuthToken = token => {
  if (token) {
    localStorage.setItem('token', token);
  } else {
    localStorage.removeItem('token');
  }
};

