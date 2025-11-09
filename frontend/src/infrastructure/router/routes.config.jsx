import { authRoutes } from "@/features/auth/routes/auth.routes.jsx";
import { dashboardRoutes } from "@/features/dashboard/routes/dashboard.routes.jsx";
import { notfoundRoutes } from "@/pages/routes/notfound.routes";
import { AuthRedirect } from "./AuthRedirect.jsx";
import { ProtectedRoute } from "./ProtectedRoute.jsx";
import { Navigate } from "react-router-dom";
import { createBrowserRouter } from "react-router-dom";

const router = [
  ...authRoutes,
  ...dashboardRoutes,
  {
    path: "/products",
    element: (
      <ProtectedRoute>
        <Navigate to="/dashboard/products" replace />
      </ProtectedRoute>
    ),
  },
  ...notfoundRoutes
];

const routerInstance = createBrowserRouter(router);

export default routerInstance;

