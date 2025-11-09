"use client";

import { useForm } from "react-hook-form";
import { useNavigate, Link, useLocation } from "react-router-dom";
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
  username: "",
  password: "",
};

export function LoginForm() {
  const form = useForm({
    defaultValues: FormSchema,
  });
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/dashboard";

  function onSubmit(data) {
    console.log("Datos enviados:", data);
    // TODO: Aquí irá la lógica de autenticación con el backend
    // Por ahora simulamos el login exitoso
    // Cuando el backend esté listo, aquí irá:
    // - Llamada a la API de login
    // - Guardar token con setAuthToken(response.data.token)
    // - Redirigir a la página desde la cual intentó acceder o dashboard
    
    // Simulación temporal - redirigir después de login exitoso
    navigate(from, { replace: true });
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
            name="username"
            rules={{
              required: "El campo esta vacio",
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
              required: "El campo esta vacio",
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
            Iniciar Sesión
          </Button>
          <div className="text-center text-sm">
            <span className="text-gray-600">¿No tienes una cuenta? </span>
            <Link
              to="/register"
              className="text-orange-600 hover:text-orange-700 font-semibold"
            >
              Regístrate aquí
            </Link>
          </div>
        </form>
      </Form>
    </div>
  );
}
