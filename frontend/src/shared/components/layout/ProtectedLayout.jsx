import { Outlet, NavLink, useNavigate } from "react-router";
import { useLogout } from "@/features/auth/hooks/useAuth.js";
import { clearAuthData } from "@/features/auth/utils/authStorage.js";
import { Button } from "@/shared/components/ui/button";

export const ProtectedLayout = () => {
  const navigate = useNavigate();
  
  const logout = useLogout({
    onSuccess: () => {
      clearAuthData();
      navigate("/login", { replace: true });
    },
    onError: (error) => {
      console.error("Error al cerrar sesión:", error.message);
      clearAuthData();
      navigate("/login", { replace: true });
    },
  });

  const handleLogout = () => {
    logout.mutate();
  };

  return (
    <div>
      <nav className="py-3 shadow-md sticky top-0 bg-slate-50">
        <div className="flex justify-between items-center px-3">
          <ul className="flex gap-3 [&>a]:p-2 [&>a]:rounded-md [&>a]:transition-all">
            <NavLink
              className={({ isActive, isPending }) =>
                ` rounded-md transition-all ${
                  isPending
                    ? "opacity-50"
                    : isActive
                    ? "bg-red-100 text-red-800"
                    : "hover:bg-gray-100"
                }`
              }
              to={"/dashboard"}
            >
              Dashboard
            </NavLink>
            <NavLink
              className={({ isActive, isPending }) =>
                ` rounded-md transition-all ${
                  isPending
                    ? "opacity-50"
                    : isActive
                    ? "bg-red-100 text-red-800"
                    : "hover:bg-gray-100"
                }`
              }
              to={"/products"}
            >
              Productos
            </NavLink>
          </ul>
          <Button
            onClick={handleLogout}
            disabled={logout.isPending}
            className="text-sm font-semibold bg-red-600 hover:bg-red-700 text-white"
          >
            {logout.isPending ? "Cerrando sesión..." : "Cerrar Sesión"}
          </Button>
        </div>
      </nav>
      <Outlet />
    </div>
  );
};

