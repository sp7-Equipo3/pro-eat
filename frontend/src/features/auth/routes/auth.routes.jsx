import LoginPage from "../pages/LoginPage.jsx";
import RegisterPage from "../pages/RegisterPage.jsx";
//import AuthLayout from "@/shared/components/layout/AuthLayout.jsx";

export const authRoutes = [
  {
    path: "/login",
    element: (
      //<AuthLayout>
        <LoginPage />
      //</AuthLayout>
    ),
  },
  {
    path: "/register",
    element: (
      //<AuthLayout>
        <RegisterPage />
      //</AuthLayout>
    ),
  },
];
