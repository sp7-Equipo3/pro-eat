import { homeRoutes } from "@/features/home/routes/home.routes";
import { createBrowserRouter } from "react-router-dom";

const router = [
  ...homeRoutes,
];

const routerInstance = createBrowserRouter(router);

export default routerInstance;

