import apiClient from '@/shared/services/apiClient.js';

export const login = async (credentials) => {
  const response = await apiClient.post('/api/auth/login', credentials);
  console.log('authService.login - Response completa:', response);
  console.log('authService.login - Response.data:', response.data);
  return response.data;
};

export const register = async (userData) => {
  const { data } = await apiClient.post('/api/auth/register', userData);
  return data;
};

export const logout = async () => {
  await apiClient.post('/api/auth/logout');
};

