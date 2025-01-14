import React from "react";
import "./ResumeTemplate2.css";
import { useLocation, useNavigate } from "react-router-dom";
import html2pdf from 'html2pdf.js';
import { ResumeUserData } from "../create-resume/ResumeUserData";

const fallbackData: ResumeUserData = {
  id: "example",
  template: 2,
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

const ResumeTemplate2: React.FC = () => {
  const location = useLocation();
  const { data } = location.state || {};

 const navigate = useNavigate(); 
  
 const handleBack = () => {
   navigate(-1); 
 };

 // Функция для скачивания PDF
 const handleDownloadPDF = () => {
   const element = document.getElementById("resume-to-pdf"); // Получаем элемент для конвертации
   if (element) {
     const opt = {
       margin:       0.2,
       filename:     'resume.pdf',
       image:        { type: 'jpeg', quality: 0.98 },
       html2canvas:  { scale: 2 },
       jsPDF:        { unit: 'in', format: 'letter', orientation: 'portrait' }
     };
     html2pdf().from(element).set(opt).save(); // Генерация и сохранение PDF
   }
 };

  return (
    <div className="resume-container2" id="resume-to-pdf">
      <div className="resume-header2">
        <h1 className="resume-name2">{data.fullName}</h1>
      </div>
      <div className="resume-content2">
        <div className="resume-section2">
          <h3>Position</h3>
          <p>{data.position}</p>
        </div>

        <div className="resume-section2">
          <h3>Personal information</h3>
          <p>Age: {data.user.age}, Gender: {data.user.gender === "male" ? "Male" : data.user.gender === "female" ? "Female" : "Other"}</p>
        </div>

        <div className="resume-section2">
          <h3>Contacts</h3>
          <p>Email: {data.user.email} Phone: {data.user.phone}</p>
        </div>

        <div className="resume-section2">
          <h3>Work experience</h3>
          <p>{data.workExperience}</p>
        </div>

        <div className="resume-section2">
          <h3>Education</h3>
          <p>{data.education}</p>
        </div>

        <div className="resume-section2">
          <h3>Skills and awards</h3>
          <p>{data.skillsAndAwards}</p>
        </div>
      </div>
     {/* Контейнер для кнопок внизу */}
     <div className="buttons-container">
        <div className="resume-back-button">
          <button onClick={handleBack} className="btn-back">
            Back
          </button>
        </div>

        <div className="resume-download-button">
          <button onClick={handleDownloadPDF} className="btn-download">
            Download as PDF
          </button>
        </div>
      </div>
    </div>
  );
};

export default ResumeTemplate2;
