import { Navigate } from 'react-router-dom';
import { getAuthToken } from '../constants/env.js';

export const PublicRoute = ({ children }) => {
  const token = getAuthToken();

  if (token) {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
};

