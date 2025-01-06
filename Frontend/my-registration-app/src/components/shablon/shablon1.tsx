import React from "react";
import "./ResumeTemplate.css";

export interface ResumeData {
  fullName: string;
  age: number;
  gender: string;
  contacts: string;
  objective: string;
  education: string;
  workExperience: string;
  skillsAndAwards: string;
  personalInfo: string;
  recommendations: string;
}

const ResumeTemplate: React.FC<{ data: ResumeData }> = ({ data }) => {
  return (
    <div className="resume-container">
      <div className="resume-header">
        <h1 className="resume-name">{data.fullName}</h1>
        <h2 className="resume-title">Менеджер проектів</h2>
      </div>
      <div className="resume-content">
        <div className="resume-section">
          <h3>ОСОБИСТА ІНФОРМАЦІЯ</h3>
          <p>{data.personalInfo}</p>
        </div>

        <div className="resume-section">
          <h3>МОЇ КОНТАКТИ</h3>
          <p>{data.contacts}</p>
        </div>

        <div className="resume-section">
          <h3>ВМІННЯ ТА НАВИЧКИ</h3>
          <p>{data.skillsAndAwards}</p>
        </div>

        <div className="resume-section">
          <h3>ІНШІ ВМІННЯ</h3>
          <p>{data.recommendations}</p>
        </div>

        <div className="resume-section">
          <h3>ДОСВІД РОБОТИ</h3>
          <p>{data.workExperience}</p>
        </div>

        <div className="resume-section">
          <h3>ОСВІТА ТА ПІДГОТОВКА</h3>
          <p>{data.education}</p>
        </div>
      </div>
    </div>
  );
};

export default ResumeTemplate;
