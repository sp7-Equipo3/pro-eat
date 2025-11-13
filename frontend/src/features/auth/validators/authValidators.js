import { z } from 'zod';

export const loginSchema = z.object({
  username: z
    .string()
    .min(1, 'El username es obligatorio')
    .min(3, 'El username debe tener al menos 3 caracteres')
    .regex(
      /^[a-zA-Z0-9_]+$/,
      'El nombre de usuario solo puede contener letras, números y guiones bajos'
    ),
  password: z
    .string()
    .min(1, 'La contraseña es obligatoria')
    .min(8, 'La contraseña debe tener entre 8 y 64 caracteres')
    .max(64, 'La contraseña debe tener entre 8 y 64 caracteres')
    .regex(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/,
      'La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un número, y un carácter especial'
    ),
});

export const registerSchema = z.object({
  username: z
    .string()
    .min(1, 'El username es obligatorio')
    .min(3, 'El username debe tener al menos 3 caracteres')
    .max(20, 'El username no puede tener más de 20 caracteres')
    .regex(
      /^[a-zA-Z0-9_]+$/,
      'El nombre de usuario solo puede contener letras, números y guiones bajos'
    ),
  password: z
    .string()
    .min(1, 'La contraseña es obligatoria')
    .min(8, 'La contraseña debe tener entre 8 y 64 caracteres')
    .max(64, 'La contraseña debe tener entre 8 y 64 caracteres')
    .regex(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/,
      'La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un número, y un carácter especial'
    ),
});

