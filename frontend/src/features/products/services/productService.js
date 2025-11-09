import apiClient from '@/shared/services/apiClient.js';

export const getProducts = async () => {
  const { data } = await apiClient.get('/products');
  return data;
};

export const getProductById = async (id) => {
  const { data } = await apiClient.get(`/products/${id}`);
  return data;
};

export const getProductsFromFakeStore = async () => {
  const response = await fetch('https://fakestoreapi.com/products');
  if (!response.ok) {
    throw new Error('Error al obtener los productos');
  }
  return response.json();
};

