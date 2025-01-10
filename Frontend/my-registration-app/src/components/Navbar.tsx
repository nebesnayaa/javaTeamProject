import React from 'react';
import { Link } from 'react-router-dom';
import './Style/Navbar.css';

interface NavbarProps {
  isAuthenticated: boolean;
  onLogout: () => void;
}

const Navbar: React.FC<NavbarProps> = ({ isAuthenticated, onLogout }) => {
  return (
    <nav className="navbar">
      <Link to="/">Home</Link>
      {!isAuthenticated ? (
        <div>
          <Link to="/register">Sign up</Link>
          <Link to="/login">Sign in</Link>
          <Link to="/profile">Profile</Link> {/* Для тестування, потім прибрати*/}
        </div>
      ) : (
        <>
          <Link to="/users">Все пользователи</Link>
          <Link to="/resume">Resume</Link>
          <Link to="/profile">Profile</Link> 
        </>
      )}
    </nav>
  );
};

export default Navbar;
