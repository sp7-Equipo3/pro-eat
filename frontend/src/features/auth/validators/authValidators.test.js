import { describe, test, expect } from 'vitest'
import { loginSchema, registerSchema } from './authValidators.js'

describe('authValidators', () => {
  describe('loginSchema', () => {
    test('debe aceptar datos válidos', () => {
      const validData = {
        username: 'alejandro',
        password: 'Password123!',
      }
      const result = loginSchema.safeParse(validData)
      expect(result.success).toBe(true)
      if (result.success) {
        expect(result.data).toEqual(validData)
      }
    })

    test('debe rechazar username con menos de 3 caracteres', () => {
      const invalidData = {
        username: 'ab',
        password: 'Password123!',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        expect(result.error.issues[0].message).toContain('al menos 3 caracteres')
      }
    })

    test('debe rechazar username con caracteres especiales', () => {
      const invalidData = {
        username: 'alejandro!',
        password: 'Password123!',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        expect(result.error.issues[0].message).toContain(
          'letras, números y guiones bajos'
        )
      }
    })

    test('debe aceptar username con guiones bajos', () => {
      const validData = {
        username: 'alejandro_123',
        password: 'Password123!',
      }
      const result = loginSchema.safeParse(validData)
      expect(result.success).toBe(true)
    })

    test('debe rechazar password con menos de 8 caracteres', () => {
      const invalidData = {
        username: 'alejandro',
        password: 'Pass1!',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const passwordError = result.error.issues.find(
          issue => issue.path[0] === 'password'
        )
        expect(passwordError?.message).toContain('8 y 64 caracteres')
      }
    })

    test('debe rechazar password mayor a 64 caracteres', () => {
      const invalidData = {
        username: 'alejandro',
        password: 'P'.repeat(65) + 'ass123!',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const passwordError = result.error.issues.find(
          issue => issue.path[0] === 'password'
        )
        expect(passwordError?.message).toContain('8 y 64 caracteres')
      }
    })

    test('debe rechazar password sin letra mayúscula', () => {
      const invalidData = {
        username: 'alejandro',
        password: 'password123!',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const passwordError = result.error.issues.find(
          issue => issue.path[0] === 'password'
        )
        expect(passwordError?.message).toContain('mayúscula')
      }
    })

    test('debe rechazar password sin letra minúscula', () => {
      const invalidData = {
        username: 'alejandro',
        password: 'PASSWORD123!',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const passwordError = result.error.issues.find(
          issue => issue.path[0] === 'password'
        )
        expect(passwordError?.message).toContain('minúscula')
      }
    })

    test('debe rechazar password sin número', () => {
      const invalidData = {
        username: 'alejandro',
        password: 'Password!',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const passwordError = result.error.issues.find(
          issue => issue.path[0] === 'password'
        )
        expect(passwordError?.message).toContain('número')
      }
    })

    test('debe rechazar password sin carácter especial', () => {
      const invalidData = {
        username: 'alejandro',
        password: 'Password123',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const passwordError = result.error.issues.find(
          issue => issue.path[0] === 'password'
        )
        expect(passwordError?.message).toContain('carácter especial')
      }
    })

    test('debe rechazar cuando username está vacío', () => {
      const invalidData = {
        username: '',
        password: 'Password123!',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe rechazar cuando password está vacío', () => {
      const invalidData = {
        username: 'alejandro',
        password: '',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe rechazar cuando username es undefined', () => {
      const invalidData = {
        password: 'Password123!',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe rechazar cuando password es undefined', () => {
      const invalidData = {
        username: 'alejandro',
      }
      const result = loginSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe verificar mensaje de error para username obligatorio', () => {
      const invalidData = {
        username: '',
        password: 'Password123!',
      }
      const result = loginSchema.safeParse(invalidData)
      if (!result.success) {
        const usernameError = result.error.issues.find(
          issue => issue.path[0] === 'username'
        )
        expect(usernameError?.message).toContain('obligatorio')
      }
    })
  })

  describe('registerSchema', () => {
    test('debe aceptar datos válidos', () => {
      const validData = {
        username: 'alejandro',
        password: 'Password123!',
      }
      const result = registerSchema.safeParse(validData)
      expect(result.success).toBe(true)
      if (result.success) {
        expect(result.data).toEqual(validData)
      }
    })

    test('debe rechazar username mayor a 20 caracteres', () => {
      const invalidData = {
        username: 'a'.repeat(21),
        password: 'Password123!',
      }
      const result = registerSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        expect(result.error.issues[0].message).toContain('más de 20 caracteres')
      }
    })

    test('debe aceptar username con exactamente 20 caracteres', () => {
      const validData = {
        username: 'a'.repeat(20),
        password: 'Password123!',
      }
      const result = registerSchema.safeParse(validData)
      expect(result.success).toBe(true)
    })

    test('debe rechazar password mayor a 64 caracteres', () => {
      const invalidData = {
        username: 'alejandro',
        password: 'P'.repeat(65) + 'ass123!',
      }
      const result = registerSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
      if (!result.success) {
        const passwordError = result.error.issues.find(
          issue => issue.path[0] === 'password'
        )
        expect(passwordError?.message).toContain('8 y 64 caracteres')
      }
    })

    test('debe aceptar password con exactamente 64 caracteres', () => {
      const validData = {
        username: 'alejandro',
        password: 'P'.repeat(60) + 'ass123!',
      }
      const result = registerSchema.safeParse(validData)
      expect(result.success).toBe(true)
    })

    test('debe aplicar todas las validaciones de loginSchema', () => {
      const invalidData = {
        username: 'ab',
        password: 'Pass1!',
      }
      const result = registerSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })

    test('debe rechazar username con caracteres especiales (igual que loginSchema)', () => {
      const invalidData = {
        username: 'alejandro!',
        password: 'Password123!',
      }
      const result = registerSchema.safeParse(invalidData)
      expect(result.success).toBe(false)
    })
  })
})

