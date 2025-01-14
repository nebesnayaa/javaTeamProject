import React from "react";
import "./ResumeTemplate3.css";
import { useLocation, useNavigate } from "react-router-dom";
import html2pdf from 'html2pdf.js'; 

const ResumeTemplate3: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate(); 
  const { data } = location.state || {};

  if (!data) {
    return <p>Loading...</p>; // Можна відобразити повідомлення, якщо дані ще не завантажені
  }
  
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
    <div className="resume-container3" id="resume-to-pdf">
     <div className="upper-line3"></div>
      <div className="resume-header3">
        <h1 className="resume-name3">{data.fullName}</h1>
        <p className="resume-age-gender3">
          Age: {data.user.age}, Gender: {data.user.gender === "male" ? "Male" : data.user.gender === "female" ? "Female" : "Other"}
        </p>
      </div>

      <div className="resume-content3">
        <div className="resume-left3">
          <div className="resume-section3">
            <h3>Contacts</h3>
            <p>Email: {data.user.email} Phone: {data.user.phone}</p>
          </div>

          <div className="resume-section3">
            <h3>Position</h3>
            <p>{data.position}</p>
          </div>

          <div className="resume-section3">
            <h3>Skills and awards</h3>
            <p>{data.skillsAndAwards}</p>
          </div>

          <div className="resume-section3">
            <h3>Languages</h3>
            <p>{data.languages}</p>
          </div>

          <div className="resume-section3">
            <h3>Recommendation</h3>
            <p>{data.recommendations}</p>
          </div>
        </div>

        <div className="resume-right3">
          <div className="resume-section3">
            <h3>Work Expirience</h3>
            <p>{data.workExperience}</p>
          </div>

          <div className="resume-section3">
            <h3>Education</h3>
            <p>{data.education}</p>
          </div>

          <div className="resume-section3">
            <h3>Hobbies and interests</h3>
            <p>{data.hobbiesAndInterests}</p>
          </div>
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

export default ResumeTemplate3;
