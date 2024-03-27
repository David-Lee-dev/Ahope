import Link from 'next/link';
import { DarkThemeToggle, Navbar, NavbarBrand, NavbarCollapse, NavbarLink, NavbarToggle } from 'flowbite-react';
import { HeaderProps } from './types';

const Header: React.FC<HeaderProps> = ({ activeTap }) => {
  return (
    <div>
      <Navbar fluid>
        <NavbarBrand as={Link} href='/'>
          <span className='self-center whitespace-nowrap text-xl font-semibold dark:text-white'>ToolBox</span>
        </NavbarBrand>
        <NavbarCollapse>
          <NavbarLink as={Link} href='/' active={activeTap === 'Home'}>
            Home
          </NavbarLink>
          <NavbarLink as={Link} href='/image' active={activeTap === 'Image'}>
            Image
          </NavbarLink>
        </NavbarCollapse>
      </Navbar>
      <div className='fixed rounded-md bg-gray-700 dark:bg-gray-300' style={{ bottom: 10, right: 10 }}>
        <DarkThemeToggle />
      </div>
    </div>
  );
};

export default Header;
