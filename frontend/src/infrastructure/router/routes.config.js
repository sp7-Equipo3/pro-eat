import { homeRoutes } from "@/features/home/routes/home.routes";
import { notfoundRoutes } from "@/pages/routes/notfound.routes";
import { createBrowserRouter } from "react-router-dom";

const router = [
  ...homeRoutes,
  ...notfoundRoutes
];

const routerInstance = createBrowserRouter(router);

export default routerInstance;

