import React from 'react';
import { Link } from 'react-router-dom';
import "./Style/Main.css";


const Main: React.FC = () => {
  return (
    <div className="main-container">
      <h1>Добро пожаловать!</h1>
      <p>Мы рады видеть вас на нашем сайте.</p>

      <Link to="/">Создать резюме</Link>
    </div>
  );
};

export default Main;
