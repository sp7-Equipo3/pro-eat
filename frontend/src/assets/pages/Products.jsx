import { useEffect, useState } from "react";
import { ProductCard } from "../components/ProductCard";


export const Products = () =>{

  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch("https://fakestoreapi.com/products")//consumimos la API
      .then((res) => {
        if (!res.ok) throw new Error("Error al obtener los productos");
        return res.json();
      })
      .then((data) => {
        console.log("Datos recibidos correctamente:", data);
        setProducts(data);
      })
      .catch((err) => {
        console.error("Error al obtener los datos: ", err);
        setError(err.message);
      })
      .finally(() => {
        console.log("Finalizó la petición a la API.");
        setLoading(false);
      });
 }, []);
  if (loading)
    return <p className="text-center text-lg mt-10">Cargando productos...</p>;

  if (error)
    return <p className="text-center text-red-500 mt-10">Error: {error}</p>;

  if (products.length === 0)
    return <p className="text-center mt-10">No hay productos disponibles.</p>;

  return(

   <div className="p-6">
      <h1 className="text-3xl font-bold text-center mb-8">
        Productos
      </h1>
      <div className="grid gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
        {products.map((product) => (
          <ProductCard key={product.id} {...product} />
        ))}
      </div>
    </div>
  );



}
