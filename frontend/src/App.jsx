
import { Products } from "./assets/pages/products"
import { RouterProvider } from "react-router-dom";
import  router  from "@/infrastructure/router/routes.config";

function App() {

  return (
    <>
      <div className="w-lvw h-lvh flex justify-center items-center flex-col bg-black">
        <h1 className="text-white text-5xl">Hello World</h1>
      </div>
    </>
  )
}
      <div className="min-h-screen bg-gray-100">
        <Products/>
      </div>
    <RouterProvider router={router} />;
    </>
  )

export default App
