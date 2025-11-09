import { NavLink } from 'react-router';
import { LayoutDashboard, Package, UtensilsCrossed } from 'lucide-react';
import { UserMenu } from './UserMenu.jsx';

export const Navbar = () => {
  return (
    <nav className='py-3 px-3 shadow-md sticky top-0 bg-slate-50'>
      <div className='flex justify-between items-center px-3'>
        <div className='flex items-center gap-4'>
          <NavLink
            to='/dashboard'
            className='flex items-center gap-2 text-orange-600 hover:text-orange-700 transition-colors'
          >
            <UtensilsCrossed className='h-6 w-6' />
            <span className='text-xl font-bold'>ProEat</span>
          </NavLink>
        </div>
        <ul className='flex gap-3 [&>a]:p-2 [&>a]:rounded-md [&>a]:transition-all'>
          <NavLink
            className={({ isActive, isPending }) =>
              `flex items-center gap-2 rounded-md transition-all ${
                isPending
                  ? 'opacity-50'
                  : isActive
                  ? 'bg-red-100 text-red-800'
                  : 'hover:bg-gray-100'
              }`
            }
            to={'/dashboard'}
          >
            <LayoutDashboard className='h-4 w-4' />
            Dashboard
          </NavLink>
          <NavLink
            className={({ isActive, isPending }) =>
              `flex items-center gap-2 rounded-md transition-all ${
                isPending
                  ? 'opacity-50'
                  : isActive
                  ? 'bg-red-100 text-red-800'
                  : 'hover:bg-gray-100'
              }`
            }
            to={'/products'}
          >
            <Package className='h-4 w-4' />
            Productos
          </NavLink>
        </ul>
        <UserMenu />
      </div>
    </nav>
  );
};
