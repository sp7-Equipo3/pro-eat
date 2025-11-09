import { useQuery } from '@tanstack/react-query';
import { ProductCard } from '../components/ProductCard.jsx';
import { getProductsFromFakeStore } from '../services/productService.js';

export default function ProductsListPage() {
  const { data: products, isLoading, error } = useQuery({
    queryKey: ['products'],
    queryFn: getProductsFromFakeStore,
    staleTime: 60 * 1000,
    refetchOnWindowFocus: false,
  });

  if (isLoading) {
    return <p className="text-center text-lg mt-10">Cargando productos...</p>;
  }

  if (error) {
    return (
      <p className="text-center text-red-500 mt-10">
        Error: {error.message}
      </p>
    );
  }

  if (!products || products.length === 0) {
    return (
      <p className="text-center mt-10">No hay productos disponibles.</p>
    );
  }

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold text-center mb-8">Productos</h1>
      <div className="grid gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
        {products.map((product) => (
          <ProductCard key={product.id} {...product} />
        ))}
      </div>
    </div>
  );
}

