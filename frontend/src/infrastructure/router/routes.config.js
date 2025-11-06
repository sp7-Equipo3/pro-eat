import { authRoutes } from "@/features/auth/routes/auth.routes";
import { homeRoutes } from "@/features/home/routes/home.routes";
import { createBrowserRouter } from "react-router-dom";

const router = [
    ...authRoutes,
    ...homeRoutes
]

const routerInstance = createBrowserRouter(router);

export default routerInstance;

