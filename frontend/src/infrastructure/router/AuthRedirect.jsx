import { Navigate } from 'react-router-dom';
import { getAuthToken } from '../constants/env.js';

export const AuthRedirect = () => {
  const token = getAuthToken();

  if (token) {
    return <Navigate to="/dashboard" replace />;
  }

  return <Navigate to="/login" replace />;
};

