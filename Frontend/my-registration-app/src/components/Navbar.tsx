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
      <Link to="/">Главная</Link>
      {!isAuthenticated ? (
        <div>
          <Link to="/register">Регистрация</Link>
          <Link to="/login">Авторизация</Link>
          <Link to="/profile">Мой профиль</Link> {/* Для тестування, потім прибрати*/}
        </div>
      ) : (
        <>
          <Link to="/users">Все пользователи</Link>
          <Link to="/resume">Резюме</Link>
          <Link to="/profile">Мой профиль</Link> 
        </>
      )}
    </nav>
  );
};

export default Navbar;
