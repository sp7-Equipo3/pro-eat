
import { RouterProvider } from "react-router-dom";
import  router  from "@/infrastructure/router/routes.config";
import { Products } from "./assets/pages/Products";

function App() {

  return (
    <>
      <RouterProvider router={router} />;
    </>
  )}

export default App;
