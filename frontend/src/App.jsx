
import { RouterProvider } from "react-router-dom";
import  router  from "@/infrastructure/router/routes.config";
import { Products } from "./assets/pages/Products";

function App() {

  return (
    <>
      <div className="min-h-screen bg-gray-100">
        <Products />
      </div>
    <RouterProvider router={router} />;
    </>
  )}

export default App;
