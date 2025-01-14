import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Style/Main.css'; // Подключение стилей для основной страницы
import { ResumeUserData } from './create-resume/ResumeUserData';

const fallbackData: ResumeUserData = {
  id: "example",
  template: 1,
  fullName: "John Doe",
  position: "Software Engineer",
  objective: "Looking for a challenging role",
  education: "B.Sc. in Computer Science",
  workExperience: "3 years in software development",
  skillsAndAwards: "JavaScript, React, Node.js",
  languages: "English, Spanish",
  recommendations: "Available upon request",
  hobbiesAndInterests: "Reading, Gaming",
  user: {
    age: 30,
    gender: "Male",
    email: "johndoe@example.com",
    phone: "1234567890",
  }
};

const Main: React.FC = () => {
  const navigate = useNavigate();

  const handleViewTemplate = (template: number) => {
    navigate(`/template${template}`, { state: { data: fallbackData } });
  };

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
      <Link to="/create-resume">Create first resume</Link>
      <div className='tmp-container'>
        <h3>Here you can view different templates:</h3>
        <div className='template-box'>
          <button className='btn-template'
                    onClick={() => handleViewTemplate(1)}>Template 1</button>
          <Link to="/template2" className='btn-template'>Template 2</Link>
          <Link to="/template3" className='btn-template'>Template 3</Link>
        </div>
      </div>
    </div>
  );
};

export default Main;
