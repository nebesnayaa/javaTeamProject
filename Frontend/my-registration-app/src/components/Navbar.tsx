import React from 'react';
import { Link } from 'react-router-dom';
import './Style/Navbar.css';

interface NavbarProps {
  isAuthenticated: boolean;
}

const Navbar: React.FC<NavbarProps> = ({ isAuthenticated }) => {
  return (
    <nav className="navbar">
      <Link to="/">Home</Link>
      {!isAuthenticated ? (
        <div>
          <Link to="/signup">Sign up</Link>
          <Link to="/login">Sign in</Link>
        </div>
      ) : (
        <div>
          {/* <Link to="/users">Все пользователи</Link> */}
          <Link to="/create-resume">Resume</Link>
          <Link to="/profile">Profile</Link> 
        </div>
      )}
    </nav>
  );
};

export default Navbar;
