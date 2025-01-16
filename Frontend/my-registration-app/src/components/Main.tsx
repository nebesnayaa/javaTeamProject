import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Style/Main.css'; // Подключение стилей для основной страницы
import { ResumeUserData } from './create-resume/ResumeUserData';

const fallbackData: ResumeUserData = {
  id: "example",
  template: 0,
  fullName: "John Doe",
  position: "Software Engineer",
  objective: "Passionate software engineer with 5+ years of experience in full-stack development. Seeking a challenging role in a dynamic team.",
  education: "B.S. in Computer Science, University of Tech, 2018",
  workExperience: "Software Developer at Tech Solutions Inc. (2018–2023). Developed and maintained web applications using JavaScript, React, and Node.js.",
  skillsAndAwards: "Skills: JavaScript, React, Node.js, MongoDB. Award: Employee of the Year 2022.",
  languages: "English (Fluent), Spanish (Intermediate)",
  recommendations: "Highly recommended for his exceptional problem-solving skills and strong team collaboration.",
  hobbiesAndInterests: "Hiking, Traveling, Playing Chess",
  user: {
    age: 29,
    gender: "Male",
    email: "johndoe@example.com",
    phone: "+1234567890"
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

      <p className='text'>You should login or sign up to start creating your perfect resume.</p>

      <div className='tmp-container'>
        <h3>Here you can view different templates:</h3>
        <div className='template-box'>
          <button className='btn-template'
                  onClick={() => handleViewTemplate(1)}>Template 1</button>
          <button className='btn-template'
                  onClick={() => handleViewTemplate(2)}>Template 2</button>
          <button className='btn-template'
                  onClick={() => handleViewTemplate(3)}>Template 3</button>
        </div>
      </div>
    </div>
  );
};

export default Main;
