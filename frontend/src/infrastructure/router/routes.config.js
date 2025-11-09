import { homeRoutes } from "@/features/home/routes/home.routes";
import { productsRoutes } from "@/features/products/routes/products.routes.jsx";
import { notfoundRoutes } from "@/pages/routes/notfound.routes";
import { createBrowserRouter } from "react-router-dom";

const router = [
  ...homeRoutes,
  ...productsRoutes,
  ...notfoundRoutes
];

const routerInstance = createBrowserRouter(router);

export default routerInstance;

