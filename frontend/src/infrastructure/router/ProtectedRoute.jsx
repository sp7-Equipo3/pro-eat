import { Navigate, useLocation } from 'react-router-dom';
import { getAuthToken } from '@/features/auth/utils/authStorage.js';

export const ProtectedRoute = ({ children }) => {
  const location = useLocation();
  const token = getAuthToken();

  if (!token) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  return children;
};

