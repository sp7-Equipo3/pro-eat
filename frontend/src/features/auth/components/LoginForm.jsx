"use client";

import { useForm } from "react-hook-form";
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

  function onSubmit(data) {
    console.log("Datos enviados:", data);
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
            className="text-sm font-semibold bg-orange-600 hover:bg-yellow-900 text-white"
            type="submit"
          >
            Enviar
          </Button>
        </form>
      </Form>
    </div>
  );
}
