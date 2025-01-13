import React from 'react';
import { Link } from 'react-router-dom';
import './Style/Main.css'; // Подключение стилей для основной страницы

const Main: React.FC = () => {
  return (
    <div className="main-container">
      <h1>Welcome on Resume Builder!</h1>
      
      {/* Блок анимации */}
      <div>
        <p> </p>
      </div>
      <div aria-busy="true" aria-label="Loading" role="progressbar" className="container">
        <div className="swing">
          <div className="swing-l"></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div className="swing-r"></div>
        </div>
        <div className="shadow">
          <div className="shadow-l"></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div></div>
          <div className="shadow-r"></div>
        </div>
      </div>

      {/* Ссылка на создание резюме */}
      <Link to="/resume">Create first resume</Link>
    </div>
  );
};

export default Main;
