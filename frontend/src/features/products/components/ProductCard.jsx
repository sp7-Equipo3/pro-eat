export const ProductCard = ({ name, price, description, category, image }) => {
  return (
    <div className='bg-white border rounded-2xl shadow-md p-4 hover:shadow-lg transition'>
      {image && (
        <img
          src={image}
          alt={name}
          className='h-40 mx-auto object-contain mb-4'
        />
      )}
      <div className='mb-2'>
        {category && (
          <span className='text-xs text-gray-500 bg-gray-100 px-2 py-1 rounded'>
            {category}
          </span>
        )}
      </div>
      <h2 className='font-semibold text-lg mb-2 line-clamp-2'>{name}</h2>
      <p className='text-gray-600 text-sm mb-2 line-clamp-3'>{description}</p>
      <p className='font-bold text-blue-600'>${price}</p>
    </div>
  );
};
