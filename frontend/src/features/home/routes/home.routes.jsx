import HomePage from "../pages/HomePage";
import { authRoutes } from "@/features/auth/routes/auth.routes";
//import { MainLayout } from "@/shared/components/layout/MainLayout.jsx";

export const homeRoutes = [
  {
    path: "/",
    element: (
      //<MainLayout>
      <HomePage />
      //</MainLayout>
    ),
    children: [
      ...authRoutes
    ],
  },
];
