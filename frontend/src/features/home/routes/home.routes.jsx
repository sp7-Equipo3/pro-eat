import HomePage from "../pages/HomePage";
//import { MainLayout } from "@/shared/components/layout/MainLayout.jsx";

export const homeRoutes = [
  {
    path: "/",
    element: (
      //<MainLayout>
        <HomePage />
      //</MainLayout>
    ),
  },
  {
    path: "/home",
    element: (
      //<MainLayout>
        <HomePage />
      //</MainLayout>
    ),
  },
];
