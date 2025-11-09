import { authRoutes } from "@/features/auth/routes/auth.routes.jsx";
import { dashboardRoutes } from "@/features/dashboard/routes/dashboard.routes.jsx";
import { productsRoutes } from "@/features/products/routes/products.routes.jsx";
import { notfoundRoutes } from "./notfound.routes.jsx";
import { createBrowserRouter } from "react-router-dom";

const router = [
  ...authRoutes,
  ...dashboardRoutes,
  ...productsRoutes,
  ...notfoundRoutes
];

const routerInstance = createBrowserRouter(router);

export default routerInstance;

