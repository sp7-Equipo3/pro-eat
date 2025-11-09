'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { X } from 'lucide-react';
import { toast } from 'sonner';
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from '@/shared/components/ui/form';
import { Button } from '@/shared/components/ui/button';
import { Input } from '@/shared/components/ui/input';
import { Textarea } from '@/shared/components/ui/textarea';
import { Select } from '@/shared/components/ui/select';
import { useApiPost } from '@/shared/hooks/useApi.js';
import { useQueryClient } from '@tanstack/react-query';
import { productSchema } from '../validators/productValidators.js';

const CATEGORIES = [
  'Bebidas Calientes',
  'Bebidas Frías',
  'Platos Fuertes',
  'Ensaladas',
  'Postres',
  'Aperitivos',
];

export function CreateProductForm({ onClose }) {
  const queryClient = useQueryClient();

  const form = useForm({
    resolver: zodResolver(productSchema),
    defaultValues: {
      name: '',
      description: '',
      price: 0,
      category: '',
    },
  });

  const createProductMutation = useApiPost('/api/products', {
    onSuccess: () => {
      queryClient.invalidateQueries(['products']);
      toast.success('Producto creado exitosamente');
      form.reset();
      onClose?.();
    },
    onError: (error) => {
      toast.error('Error al crear producto', {
        description: error.message || 'Por favor, intenta de nuevo.',
      });
    },
  });

  const onSubmit = (data) => {
    createProductMutation.mutate(data);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <div className="sticky top-0 bg-white border-b px-6 py-4 flex items-center justify-between">
          <h2 className="text-2xl font-bold">Crear Nuevo Producto</h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700"
            aria-label="Cerrar"
          >
            <X className="h-6 w-6" />
          </button>
        </div>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="p-6 space-y-6">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nombre del Producto</FormLabel>
                  <FormControl>
                    <Input placeholder="Ej: Hamburguesa Clásica" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Descripción</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Describe el producto..."
                      rows={4}
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <div className="grid grid-cols-2 gap-4">
              <FormField
                control={form.control}
                name="price"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Precio</FormLabel>
                    <FormControl>
                      <Input
                        type="number"
                        step="0.01"
                        min="0"
                        placeholder="0.00"
                        {...field}
                        onChange={(e) => field.onChange(parseFloat(e.target.value) || 0)}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="category"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Categoría</FormLabel>
                    <FormControl>
                      <Select {...field}>
                        <option value="">Selecciona una categoría</option>
                        {CATEGORIES.map((category) => (
                          <option key={category} value={category}>
                            {category}
                          </option>
                        ))}
                      </Select>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="flex gap-4 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={onClose}>
                Cancelar
              </Button>
              <Button type="submit" disabled={createProductMutation.isPending}>
                {createProductMutation.isPending ? 'Creando...' : 'Crear Producto'}
              </Button>
            </div>
          </form>
        </Form>
      </div>
    </div>
  );
}

