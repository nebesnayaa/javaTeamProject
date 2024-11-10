import React from 'react';
import { Link } from 'react-router-dom';
import './Style/Navbar.css'

interface NavbarProps {
  isAuthenticated: boolean;
  onLogout: () => void;
}

const Navbar: React.FC<NavbarProps> = ({ isAuthenticated, onLogout }) => {
  return (
    <nav className="navbar">
      <Link to="/">Главная</Link>
      {!isAuthenticated ? (
        <>
          <Link to="/register">Регистрация</Link>
          <Link to="/login">Авторизация</Link>
        </>
      ) : (
        <button onClick={onLogout}>Logout</button>
      )}
    </nav>
  );
};

export default Navbar;
