import { useState, useEffect, useMemo } from 'react';
import { useApiQuery } from '@/shared/hooks/useApi.js';
import { ProductCard } from '../components/ProductCard.jsx';
import { Pagination } from '@/shared/components/ui/pagination.jsx';
import { ProductsFiltersSidebar } from '../components/ProductsFiltersSidebar.jsx';
import { CreateProductForm } from '../components/CreateProductForm.jsx';
import { Button } from '@/shared/components/ui/button';
import { Filter, Plus } from 'lucide-react';

export default function ProductsListPage() {
  const [page, setPage] = useState(0);
  const [size] = useState(20);
  const [isFiltersOpen, setIsFiltersOpen] = useState(true);
  const [showCreateForm, setShowCreateForm] = useState(false);

  const [searchQuery, setSearchQuery] = useState('');

  const [filters, setFilters] = useState({
    searchQuery: '',
    sort: '',
    selectedCategories: []
  });

  useEffect(() => {
    setFilters(prev => ({ ...prev, searchQuery }));
  }, [searchQuery]);

  const queryParams = useMemo(() => {
    const params = {
      page,
      size
    };

    if (filters.sort) {
      params.sort = filters.sort;
    }

    if (filters.selectedCategories.length > 0) {
      params.category = filters.selectedCategories;
    }

    return params;
  }, [page, size, filters.sort, filters.selectedCategories]);

  const queryString = useMemo(() => {
    const queryParamsObj = new URLSearchParams();
    Object.entries(queryParams).forEach(([key, value]) => {
      if (Array.isArray(value)) {
        value.forEach(v => queryParamsObj.append(key, v));
      } else if (value !== undefined && value !== null && value !== '') {
        queryParamsObj.append(key, value.toString());
      }
    });
    return `/api/products?${queryParamsObj.toString()}`;
  }, [queryParams]);

  const {
    data: paginatedResponse,
    isLoading,
    isFetching,
    error
  } = useApiQuery(['products', queryParams], queryString, {
    placeholderData: previousData => previousData,
    staleTime: 30 * 1000
  });

  const allProducts = useMemo(
    () => paginatedResponse?.content || [],
    [paginatedResponse?.content]
  );

  const filteredProducts = useMemo(() => {
    let filtered = [...allProducts];

    if (filters.searchQuery.trim()) {
      const searchLower = filters.searchQuery.toLowerCase().trim();
      filtered = filtered.filter(
        product =>
          product.name?.toLowerCase().includes(searchLower) ||
          product.description?.toLowerCase().includes(searchLower)
      );
    }

    if (filters.selectedCategories.length > 0) {
      filtered = filtered.filter(product =>
        filters.selectedCategories.includes(product.category)
      );
    }

    if (filters.sort) {
      const [field, direction] = filters.sort.split(',');
      filtered.sort((a, b) => {
        let aValue = a[field];
        let bValue = b[field];

        if (typeof aValue === 'string') {
          aValue = aValue.toLowerCase();
          bValue = bValue.toLowerCase();
        }

        if (direction === 'asc') {
          return aValue > bValue ? 1 : aValue < bValue ? -1 : 0;
        } else {
          return aValue < bValue ? 1 : aValue > bValue ? -1 : 0;
        }
      });
    }

    return filtered;
  }, [
    allProducts,
    filters.searchQuery,
    filters.selectedCategories,
    filters.sort
  ]);

  const products = filteredProducts;
  const totalPages = paginatedResponse?.totalPages || 0;

  useEffect(() => {
    setPage(0);
  }, [filters.sort, filters.selectedCategories]);

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

  const handleCategoriesChange = categories => {
    setFilters(prev => ({ ...prev, selectedCategories: categories }));
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
        selectedCategories={filters.selectedCategories}
        onCategoriesChange={handleCategoriesChange}
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
              <Button onClick={() => setShowCreateForm(true)}>
                <Plus className='h-4 w-4 mr-2' />
                Crear Producto
              </Button>
            </div>
          </div>

          {isLoading && !paginatedResponse ? (
            <p className='text-center text-lg mt-10'>Cargando productos...</p>
          ) : !allProducts || allProducts.length === 0 ? (
            <p className='text-center mt-10'>No hay productos disponibles.</p>
          ) : !products || products.length === 0 ? (
            <p className='text-center mt-10'>
              {filters.searchQuery
                ? `No se encontraron productos que coincidan con "${filters.searchQuery}"`
                : 'No hay productos que coincidan con los filtros seleccionados.'}
            </p>
          ) : (
            <>
              {isFetching && paginatedResponse && (
                <div className='mb-4 text-center text-sm text-gray-500'>
                  Actualizando...
                </div>
              )}
              {filters.searchQuery && (
                <div className='mb-4 text-sm text-gray-600'>
                  Mostrando {products.length} de {allProducts.length} productos
                </div>
              )}
              <div className='grid gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 mb-8'>
                {products.map(product => (
                  <ProductCard key={product.id} {...product} />
                ))}
              </div>
              {!filters.searchQuery && (
                <Pagination
                  currentPage={page}
                  totalPages={totalPages}
                  totalElements={paginatedResponse?.totalElements || 0}
                  pageSize={size}
                  onPageChange={handlePageChange}
                />
              )}
            </>
          )}
        </div>
      </div>

      {showCreateForm && (
        <CreateProductForm onClose={() => setShowCreateForm(false)} />
      )}
    </div>
  );
}
