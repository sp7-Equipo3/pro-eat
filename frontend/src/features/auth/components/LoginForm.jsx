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
} from "@/shared/components/ui/form";
import { Button } from "@/shared/components/ui/button";
import { useLogin } from "../hooks/useAuth.js";
import { setAuthToken, clearAuthData } from "../utils/authStorage.js";

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

  const login = useLogin({
    onSuccess: (data) => {
      console.log("Login success - Data recibida:", data);
      if (data && data.token) {
        console.log("Token encontrado, guardando...");
        setAuthToken(data.token);
        console.log("Token guardado, navegando a:", from);
        navigate(from, { replace: true });
      } else {
        console.warn("No se recibió token en la respuesta:", data);
        clearAuthData();
        form.setError("root", {
          message: "No se recibió token de autenticación. Por favor, intenta de nuevo.",
        });
      }
    },
    onError: (error) => {
      console.error("Error al iniciar sesión:", error);
      clearAuthData();
      form.setError("root", {
        message: error?.message || "Error al iniciar sesión. Por favor, intenta de nuevo.",
      });
    },
  });

  function onSubmit(data) {
    console.log('LoginForm - onSubmit llamado con:', data);
    login.mutate(data);
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
          {form.formState.errors.root && (
            <div className="text-red-500 text-sm text-center">
              {form.formState.errors.root.message}
            </div>
          )}
          <Button
            className="w-full text-sm font-semibold bg-orange-600 hover:bg-yellow-900 text-white"
            type="submit"
            disabled={login.isPending}
          >
            {login.isPending ? "Iniciando sesión..." : "Iniciar Sesión"}
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
