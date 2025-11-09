import DashboardPage from "../pages/DashboardPage.jsx";
import ProductsListPage from "@/features/products/pages/ProductsListPage.jsx";
import { ProtectedRoute } from "@/infrastructure/router/ProtectedRoute.jsx";

export const dashboardRoutes = [
  {
    path: "/dashboard",
    element: (
      <ProtectedRoute>
        <DashboardPage />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: (
          <div className="p-6">
            <h1 className="text-3xl font-bold mb-4">Dashboard</h1>
            <p className="text-gray-600">Bienvenido al sistema POS gastronómico</p>
          </div>
        ),
      },
      {
        path: "products",
        element: <ProductsListPage />,
      },
    ],
  },
];

