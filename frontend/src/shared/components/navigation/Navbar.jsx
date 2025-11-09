import { NavLink, useNavigate } from "react-router";
import { LayoutDashboard, Package, LogOut } from "lucide-react";
import { useLogout } from "@/features/auth/hooks/useAuth.js";
import { clearAuthData, getUsername } from "@/features/auth/utils/authStorage.js";
import { Button } from "@/shared/components/ui/button";

export const Navbar = () => {
  const navigate = useNavigate();
  const username = getUsername();
  
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
    <nav className="py-3 shadow-md sticky top-0 bg-slate-50">
      <div className="flex justify-between items-center px-3">
        <ul className="flex gap-3 [&>a]:p-2 [&>a]:rounded-md [&>a]:transition-all">
          <NavLink
            className={({ isActive, isPending }) =>
              `flex items-center gap-2 rounded-md transition-all ${
                isPending
                  ? "opacity-50"
                  : isActive
                  ? "bg-red-100 text-red-800"
                  : "hover:bg-gray-100"
              }`
            }
            to={"/dashboard"}
          >
            <LayoutDashboard className="h-4 w-4" />
            Dashboard
          </NavLink>
          <NavLink
            className={({ isActive, isPending }) =>
              `flex items-center gap-2 rounded-md transition-all ${
                isPending
                  ? "opacity-50"
                  : isActive
                  ? "bg-red-100 text-red-800"
                  : "hover:bg-gray-100"
              }`
            }
            to={"/products"}
          >
            <Package className="h-4 w-4" />
            Productos
          </NavLink>
        </ul>
        <div className="flex items-center gap-3">
          {username && (
            <span className="text-gray-700 text-sm font-medium">
              Hola, {username}
            </span>
          )}
          <Button
            onClick={handleLogout}
            disabled={logout.isPending}
            className="text-sm font-semibold bg-red-600 hover:bg-red-700 text-white flex items-center gap-2"
          >
            <LogOut className="h-4 w-4" />
            {logout.isPending ? "Cerrando sesión..." : "Cerrar Sesión"}
          </Button>
        </div>
      </div>
    </nav>
  );
};

