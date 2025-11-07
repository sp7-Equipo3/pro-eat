import { Products } from "./assets/pages/products"
import { RouterProvider } from "react-router-dom";
import  router  from "@/infrastructure/router/routes.config";

function App() {

  return (
    <>
      <div className="min-h-screen bg-gray-100">
        <Products/>
      </div>
    <RouterProvider router={router} />;
    </>
  )}

export default App;
