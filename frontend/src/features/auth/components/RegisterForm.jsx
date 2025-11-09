"use client";

import { useForm } from "react-hook-form";
import { useNavigate, Link } from "react-router-dom";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
  FormDescription,
} from "@/shared/components/ui/form";
import { Button } from "@/shared/components/ui/button";

const FormSchema = {
  email: "",
  username: "",
  password: "",
};

export function RegisterForm() {
  const form = useForm({
    defaultValues: FormSchema,
  });
  const navigate = useNavigate();

  function onSubmit(data) {
    console.log("Datos enviados:", data);
    // TODO: Aquí irá la lógica de registro con el backend
    // Por ahora simulamos el registro exitoso
    // Cuando el backend esté listo, aquí irá:
    // - Llamada a la API de registro
    // - Guardar token
    // - Redirigir a dashboard o login
    
    // Simulación temporal - redirigir al login después de registro exitoso
    navigate("/login");
  }

  return (
    <div className="flex justify-center p-8">
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="p-6 border rounded-lg shadow-lg w-full max-w-sm space-y-8"
        >
          <FormField
            control={form.control}
            name="Email"
            rules={{
              required: "El correo electronico es obligatorio",
              pattern: {
                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                message: "Formato de correo electrónico inválido.",
              },
            }}
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-sm font-semibold text-orange-600">
                  Email
                </FormLabel>
                <FormControl>
                  <input
                    type="email"
                    className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
                    placeholder="Email"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="username"
            rules={{
              required: "El username es obligatorio",
            }}
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-sm font-semibold text-orange-600">
                  Username
                </FormLabel>
                <FormControl>
                  <input
                    className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
                    placeholder="Username"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="password"
            rules={{
              required: "La contraseña es obligatoria.",
              minLength: {
                value: 6,
                message: "La contraseña debe tener al menos 6 caracteres.",
              },
            }}
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-sm font-semibold text-orange-600">
                  Password
                </FormLabel>
                <FormControl>
                  <input
                    type="password"
                    className="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-600"
                    placeholder="Password"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button
            className="w-full text-sm font-semibold bg-orange-600 hover:bg-yellow-900 text-white"
            type="submit"
          >
            Registrarse
          </Button>
          <div className="text-center text-sm">
            <span className="text-gray-600">¿Ya tienes una cuenta? </span>
            <Link
              to="/login"
              className="text-orange-600 hover:text-orange-700 font-semibold"
            >
              Inicia sesión aquí
            </Link>
          </div>
        </form>
      </Form>
    </div>
  );
}
