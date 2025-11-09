import { RouterProvider } from "react-router-dom";
import router from "@/infrastructure/router/routes.config.jsx";

function App() {
  return (
    <>
      <RouterProvider router={router} />;
    </>
  );
}

export default App;
