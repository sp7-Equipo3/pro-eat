import LoginPage from "../pages/LoginPage.jsx";
import RegisterPage from "../pages/RegisterPage.jsx";
import { PublicRoute } from "@/infrastructure/router/PublicRoute.jsx";
//import AuthLayout from "@/shared/components/layout/AuthLayout.jsx";

export const authRoutes = [
  {
    path: "/",
    element: (
      <PublicRoute>
        {/*<AuthLayout>*/}
          <LoginPage />
        {/*</AuthLayout>*/}
      </PublicRoute>
    ),
  },
  {
    path: "/login",
    element: (
      <PublicRoute>
        {/*<AuthLayout>*/}
          <LoginPage />
        {/*</AuthLayout>*/}
      </PublicRoute>
    ),
  },
  {
    path: "/register",
    element: (
      <PublicRoute>
        {/*<AuthLayout>*/}
          <RegisterPage />
        {/*</AuthLayout>*/}
      </PublicRoute>
    ),
  },
];
