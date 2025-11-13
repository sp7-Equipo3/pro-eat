import { Check, X } from "lucide-react";

const PASSWORD_REQUIREMENTS = [
  {
    id: "length",
    label: "Entre 8 y 64 caracteres",
    test: (password) => password.length >= 8 && password.length <= 64,
  },
  {
    id: "uppercase",
    label: "Al menos una letra mayúscula",
    test: (password) => /[A-Z]/.test(password),
  },
  {
    id: "special",
    label: "Al menos un carácter especial (@$!%*?&)",
    test: (password) => /[@$!%*?&]/.test(password),
  },
];

export function PasswordRequirements({ password }) {
  if (!password) {
    return null;
  }

  return (
    <div className="mt-2 space-y-1.5">
      {PASSWORD_REQUIREMENTS.map((requirement) => {
        const isValid = requirement.test(password);
        return (
          <div
            key={requirement.id}
            className={`flex items-center gap-2 text-sm transition-colors ${
              isValid ? "text-green-600" : "text-gray-500"
            }`}
          >
            {isValid ? (
              <Check className="h-4 w-4 flex-shrink-0" />
            ) : (
              <X className="h-4 w-4 flex-shrink-0" />
            )}
            <span>{requirement.label}</span>
          </div>
        );
      })}
    </div>
  );
}

