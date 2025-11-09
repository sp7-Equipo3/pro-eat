import { X } from 'lucide-react';
import { Button } from '@/shared/components/ui/button';
import { SearchBar } from './filters/SearchBar';
import { SortSelector } from './filters/SortSelector';
import { CategoryFilter } from './filters/CategoryFilter';

export function ProductsFiltersSidebar({
  isOpen,
  onClose,
  searchQuery,
  onSearchChange,
  sort,
  onSortChange,
  selectedCategories,
  onCategoriesChange
}) {
  return (
    <>
      <div
        className={`fixed inset-0 bg-black bg-opacity-50 z-40 transition-opacity lg:hidden ${
          isOpen ? 'opacity-100' : 'opacity-0 pointer-events-none'
        }`}
        onClick={onClose}
      />
      <aside
        className={`fixed left-0 top-0 h-full w-80 bg-white shadow-xl z-50 overflow-y-auto transition-transform lg:relative lg:z-auto lg:shadow-none lg:translate-x-0 ${
          isOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <div className='sticky top-0 bg-white border-b p-4 flex items-center justify-between lg:hidden'>
          <h2 className='text-lg font-semibold'>Filtros</h2>
          <button
            onClick={onClose}
            className='text-gray-500 hover:text-gray-700'
            aria-label='Cerrar filtros'
          >
            <X className='h-6 w-6' />
          </button>
        </div>

        <div className='p-6 space-y-6'>
          <div>
            <h3 className='text-lg font-semibold mb-4'>Buscar</h3>
            <SearchBar value={searchQuery} onChange={onSearchChange} />
          </div>

          <div>
            <h3 className='text-lg font-semibold mb-4'>Ordenar</h3>
            <SortSelector value={sort} onChange={onSortChange} />
          </div>

          <div>
            <h3 className='text-lg font-semibold mb-4'>Categorías</h3>
            <CategoryFilter
              selectedCategories={selectedCategories}
              onChange={onCategoriesChange}
            />
          </div>

          <div className='pt-4 border-t lg:hidden'>
            <Button onClick={onClose} className='w-full' variant='outline'>
              Aplicar Filtros
            </Button>
          </div>
        </div>
      </aside>
    </>
  );
}
