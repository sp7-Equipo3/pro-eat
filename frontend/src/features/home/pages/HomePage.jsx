import { Link, Outlet, NavLink } from "react-router";

const HomePage = () => {
  return (
    <div>
      <nav className="py-3 shadow-md sticky top-0 bg-slate-50">
        <ul className="flex gap-3 px-3 [&>a]:p-2 [&>a]:rounded-md [&>a]:transition-all">
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
          <NavLink
            className={({ isActive, isPending }) =>
              ` ${
                isPending
                  ? "opacity-50"
                  : isActive
                  ? "bg-red-100 text-red-800"
                  : "hover:bg-gray-100"
              }`
            }
            to={"/login"}
          >
            Login
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
            to={"/register"}
          >
            Register
          </NavLink>
        </ul>
      </nav>
      <Outlet />
    </div>
  );
};

export default HomePage;
