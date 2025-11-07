export const ProductCard = ({title, price, description, image}) => {
    return (
    <div className="bg-white border rounded-2xl shadow-md p-4 hover:shadow-lg transition">
      <img
        src={image}
        alt={title}
        className="h-40 mx-auto object-contain mb-4"
      />
      <h2 className="font-semibold text-lg mb-2 line-clamp-2">{title}</h2>
      <p className="text-gray-600 text-sm mb-2 line-clamp-3">{description}</p>
      <p className="font-bold text-blue-600">${price}</p>
    </div>
  );
}