import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import './Style/Navbar.css';
import { AppContext } from '../context';

interface NavbarProps {
  isAuthenticated: boolean;
}

const Navbar: React.FC<NavbarProps> = ({ isAuthenticated }) => {
  const context = useContext(AppContext);
  return (
    <nav className="navbar">
      <Link to="/">Home</Link>
      {context?.userId==="" ? (
        <div>
          <Link to="/signup">Sign up</Link>
          <Link to="/login">Sign in</Link>
        </div>
      ) : (
        <div>
          {/* <Link to="/users">Все пользователи</Link> */}
          <Link to="/create-resume">New resume</Link>
          <Link to="/profile">Profile</Link> 
        </div>
      )}
    </nav>
  );
};

export default Navbar;
