import { useState, useEffect, useMemo } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useApiMutation } from '@/shared/hooks/useApi.js';
import { useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { ProductCard } from '../components/ProductCard.jsx';
import { Pagination } from '@/shared/components/ui/pagination.jsx';
import { ProductsFiltersSidebar } from '../components/ProductsFiltersSidebar.jsx';
import { CreateProductForm } from '../components/CreateProductForm.jsx';
import { Button } from '@/shared/components/ui/button';
import { Filter, Plus } from 'lucide-react';
import apiClient from '@/shared/services/apiClient.js';
import { getProducts } from '../services/productService.js';

export default function ProductsListPage() {
  const queryClient = useQueryClient();
  const [page, setPage] = useState(0);
  const [size] = useState(4);
  const [isFiltersOpen, setIsFiltersOpen] = useState(false);
  const [isSidebarCollapsed, setIsSidebarCollapsed] = useState(false);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [productToEdit, setProductToEdit] = useState(null);

  const [searchQuery, setSearchQuery] = useState('');

  const [filters, setFilters] = useState({
    sort: ''
  });

  const {
    data: allProductsResponse,
    isLoading,
    error
  } = useQuery({
    queryKey: ['products', 'all'],
    queryFn: () => getProducts({ page: 0, size: 1000 }),
    staleTime: 30 * 1000
  });

  const allProducts = useMemo(
    () => allProductsResponse?.data?.content || [],
    [allProductsResponse?.data?.content]
  );

  const filteredProducts = useMemo(() => {
    let filtered = [...allProducts];

    if (searchQuery?.trim()) {
      const query = searchQuery.trim().toLowerCase();
      filtered = filtered.filter(product =>
        product.name?.toLowerCase().includes(query)
      );
    }

    if (filters.sort) {
      filtered.sort((a, b) => {
        switch (filters.sort) {
          case 'name,asc':
            return (a.name || '').localeCompare(b.name || '');
          case 'name,desc':
            return (b.name || '').localeCompare(a.name || '');
          case 'price,asc':
            return (a.price || 0) - (b.price || 0);
          case 'price,desc':
            return (b.price || 0) - (a.price || 0);
          default:
            return 0;
        }
      });
    }

    return filtered;
  }, [allProducts, searchQuery, filters.sort]);

  const totalElements = filteredProducts.length;
  const totalPages = Math.ceil(totalElements / size);
  const startIndex = page * size;
  const endIndex = startIndex + size;
  const products = filteredProducts.slice(startIndex, endIndex);
  const numberOfElements = products.length;

  useEffect(() => {
    setPage(0);
  }, [filters.sort, searchQuery]);

  const handlePageChange = newPage => {
    setPage(newPage);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleSearchChange = value => {
    setSearchQuery(value);
  };

  const handleSortChange = value => {
    setFilters(prev => ({ ...prev, sort: value }));
  };

  const deleteProductMutation = useApiMutation(
    async id => {
      const response = await apiClient.delete(`/api/products/${id}`);
      return response.data;
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['products']);
        toast.success('Producto eliminado exitosamente');
      },
      onError: error => {
        if (error?.status === 403) {
          toast.error('Acceso denegado', {
            description: error?.data?.message || 'Este usuario no tiene permisos para eliminar productos. Solo los administradores pueden realizar esta acción.'
          });
        } else {
          toast.error('Error al eliminar producto', {
            description: error?.message || 'Por favor, intenta de nuevo.'
          });
        }
      }
    }
  );

  const handleEdit = product => {
    setProductToEdit(product);
    setShowCreateForm(true);
  };

  const handleDelete = (id, name) => {
    toast.custom(
      t => (
        <div className='bg-white rounded-lg shadow-lg p-4 border border-gray-200 min-w-[300px]'>
          <div className='flex flex-col gap-3'>
            <p className='font-semibold text-gray-900'>¿Eliminar producto?</p>
            <p className='text-sm text-gray-600'>
              ¿Estás seguro de que deseas eliminar "{name}"? Esta acción no se
              puede deshacer.
            </p>
            <div className='flex gap-2 justify-end'>
              <button
                onClick={() => toast.dismiss(t)}
                className='px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200 transition'
              >
                Cancelar
              </button>
              <button
                onClick={() => {
                  toast.dismiss(t);
                  deleteProductMutation.mutate(id);
                }}
                className='px-4 py-2 text-sm font-medium text-white bg-red-600 rounded-md hover:bg-red-700 transition'
              >
                Eliminar
              </button>
            </div>
          </div>
        </div>
      ),
      {
        duration: Infinity
      }
    );
  };

  const handleCloseForm = () => {
    setShowCreateForm(false);
    setProductToEdit(null);
  };

  if (error) {
    return (
      <p className='text-center text-red-500 mt-10'>Error: {error.message}</p>
    );
  }

  return (
    <div className='flex min-h-screen'>
      <ProductsFiltersSidebar
        isOpen={isFiltersOpen}
        onClose={() => setIsFiltersOpen(false)}
        searchQuery={searchQuery}
        onSearchChange={handleSearchChange}
        sort={filters.sort}
        onSortChange={handleSortChange}
        isCollapsed={isSidebarCollapsed}
        onToggleCollapse={() => setIsSidebarCollapsed(!isSidebarCollapsed)}
      />

      <div className='flex-1'>
        <div className='p-6'>
          <div className='flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8'>
            <h1 className='text-3xl font-bold'>Productos</h1>
            <div className='flex gap-2'>
              <Button
                variant='outline'
                onClick={() => setIsFiltersOpen(!isFiltersOpen)}
                className='lg:hidden'
              >
                <Filter className='h-4 w-4 mr-2' />
                Filtros
              </Button>
              <Button
                onClick={() => {
                  setProductToEdit(null);
                  setShowCreateForm(true);
                }}
                className='bg-orange-600 hover:bg-orange-700 text-white'
              >
                <Plus className='h-4 w-4 mr-2' />
                Crear Producto
              </Button>
            </div>
          </div>

          {isLoading && !allProductsResponse ? (
            <p className='text-center text-lg mt-10'>Cargando productos...</p>
          ) : totalElements === 0 ? (
            <p className='text-center mt-10'>
              {searchQuery
                ? 'No se encontraron productos que coincidan con los filtros seleccionados.'
                : 'No hay productos disponibles.'}
            </p>
          ) : (
            <>
              <div className='mb-4 text-sm text-gray-600'>
                {searchQuery ? (
                  <>
                    Mostrando {numberOfElements} de {totalElements} productos
                    {searchQuery && ` para "${searchQuery}"`}
                  </>
                ) : (
                  <>
                    Mostrando {numberOfElements} de {totalElements} productos
                  </>
                )}
              </div>
              <div className='grid gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 mb-8'>
                {products.map(product => (
                  <ProductCard
                    key={product.id}
                    {...product}
                    onEdit={handleEdit}
                    onDelete={handleDelete}
                  />
                ))}
              </div>
              <Pagination
                currentPage={page}
                totalPages={totalPages}
                totalElements={totalElements}
                pageSize={size}
                onPageChange={handlePageChange}
              />
            </>
          )}
        </div>
      </div>

      <CreateProductForm
        open={showCreateForm}
        onClose={handleCloseForm}
        product={productToEdit}
      />
    </div>
  );
}
