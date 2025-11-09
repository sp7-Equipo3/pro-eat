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
} from "@/shared/components/ui/form";
import { Button } from "@/shared/components/ui/button";
import { useRegister } from "../hooks/useAuth.js";
import { setAuthToken, clearAuthData } from "../utils/authStorage.js";

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

  const register = useRegister({
    onSuccess: (data) => {
      if (data.token) {
        setAuthToken(data.token);
        navigate("/dashboard", { replace: true });
      } else {
        clearAuthData();
        navigate("/login", { replace: true });
      }
    },
    onError: (error) => {
      console.error("Error al registrarse:", error.message);
      clearAuthData();
      form.setError("root", {
        message: error.message || "Error al registrarse. Por favor, intenta de nuevo.",
      });
    },
  });

  function onSubmit(data) {
    register.mutate(data);
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
            name="email"
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
          {form.formState.errors.root && (
            <div className="text-red-500 text-sm text-center">
              {form.formState.errors.root.message}
            </div>
          )}
          <Button
            className="w-full text-sm font-semibold bg-orange-600 hover:bg-yellow-900 text-white"
            type="submit"
            disabled={register.isPending}
          >
            {register.isPending ? "Registrando..." : "Registrarse"}
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
