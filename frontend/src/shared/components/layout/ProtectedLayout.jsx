import { Outlet } from 'react-router';
import { Navbar } from '@/shared/components/navigation/Navbar.jsx';

export const ProtectedLayout = () => {
  return (
    <div>
      <Navbar />
      <Outlet />
    </div>
  );
};
